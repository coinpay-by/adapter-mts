package by.coinpay.mts.jobs;

import by.coinpay.mts.constants.StatusMessages;
import by.coinpay.mts.enums.CoinPayTransferStatus;
import by.coinpay.mts.enums.InternalStatus;
import by.coinpay.mts.enums.TransactionStatus;
import by.coinpay.mts.models.dto.coinpay.transfer.response.CoinPayErrorResponseDto;
import by.coinpay.mts.models.dto.coinpay.transfer.response.CoinPayExecuteResponseDto;
import by.coinpay.mts.models.entity.Transfers;
import by.coinpay.mts.service.PayoutService;
import by.coinpay.mts.service.TransfersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Берёт переводы в статусе PROCESSING, ещё не отправленные в CoinPay (internal_status IS NULL), и
 * отправляет их на оплату.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendTransfersJob {

    private static final String SEND_TRANSFERS_JOB = "sendTransfers";
    private static final int MAX_INTERNAL_MESSAGE_LENGTH = 100;

    TransfersService transfersService;
    PayoutService payoutService;
    ObjectMapper objectMapper;

    Executor payoutExecutor;

    @Scheduled(cron = "${mts-adapter.jobs.send-transfers.cron}")
    @SchedulerLock(name = SEND_TRANSFERS_JOB)
    public void sendTransfers() {
        log.info("Job '{}' started", SEND_TRANSFERS_JOB);
        long startedAt = System.currentTimeMillis();

        List<Transfers> toSend = transfersService.findNotSentCoinPay(TransactionStatus.PROCESSING);
        AtomicInteger sent = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        List<CompletableFuture<Void>> futures = toSend.stream()
                .map(transfer ->
                        CompletableFuture.runAsync(() -> processTransfer(transfer, sent, failed), payoutExecutor)
                )
                .toList();

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        double durationSeconds = (System.currentTimeMillis() - startedAt) / 1000.0;
        log.info("Job '{}' finished: sent: {}, failed: {}, duration: {} s", SEND_TRANSFERS_JOB, sent.get(), failed.get(), durationSeconds);
    }

    private void processTransfer(Transfers transfer, AtomicInteger sent, AtomicInteger failed) {
        try {
            CoinPayExecuteResponseDto response = payoutService.send(transfer);
            transfer.setCoinpayTransferId(response.id());
            if (response.status() == CoinPayTransferStatus.REJECTED) {
                transfer.setInternalStatus(InternalStatus.REJECTED);
                transfer.setTransactionStatus(TransactionStatus.REJECTED);
                transfer.setStatusMessage(StatusMessages.PAYMENT_ERROR);
                transfer.setInternalMessage(response.reasonCode());
            } else {
                transfer.setInternalStatus(InternalStatus.SENT);
            }

            transfersService.update(transfer);
            sent.incrementAndGet();
        } catch (Exception e) {
            log.warn("Failed to send transfer {} to CoinPay: {}", transfer.getId(), e.getMessage(), e);
            transfer.setInternalStatus(InternalStatus.SEND_FAILED);
            transfer.setTransactionStatus(TransactionStatus.REJECTED);
            transfer.setStatusMessage(StatusMessages.PAYMENT_ERROR);
            transfer.setInternalMessage(resolveInternalMessage(e));
            transfersService.update(transfer);
            failed.incrementAndGet();
        }
    }

    private String resolveInternalMessage(Exception e) {
        if (e instanceof FeignException feignException) {
            try {
                CoinPayErrorResponseDto error = objectMapper.readValue(feignException.contentUTF8(), CoinPayErrorResponseDto.class);
                if (error.code() != null || error.message() != null) {
                    String message = String.join(" - ",
                            error.code() != null ? error.code() : "",
                            error.message() != null ? error.message() : "");

                    return message.length() > MAX_INTERNAL_MESSAGE_LENGTH
                            ? message.substring(0, MAX_INTERNAL_MESSAGE_LENGTH)
                            : message;
                }
            } catch (Exception parseError) {
                log.warn("Failed to parse CoinPay error body: {}", parseError.getMessage());
            }
        }
        return "Ошибка при отправке оплаты в CoinPay";
    }
}
