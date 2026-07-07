package by.coinpay.mts.jobs;

import by.coinpay.mts.models.dto.coinpay.transfer.response.TransferStatusResponseDto;
import by.coinpay.mts.constants.StatusMessages;
import by.coinpay.mts.enums.CoinPayTransferStatus;
import by.coinpay.mts.enums.InternalStatus;
import by.coinpay.mts.enums.TransactionStatus;
import by.coinpay.mts.models.entity.Transfers;
import by.coinpay.mts.service.PayoutService;
import by.coinpay.mts.service.TransfersService;
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
 * Опрашивает статусы переводов, отправленных в CoinPay (PROCESSING + internal_status = SENT). По
 * финальному статусу CoinPay переводит transaction_status в PROCESSED / REJECTED.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CheckTransfersStatusJob {

    private static final String CHECK_TRANSFERS_STATUS_JOB = "checkTransfersStatus";
    private static final int MAX_INTERNAL_MESSAGE_LENGTH = 100;

    TransfersService transfersService;
    PayoutService payoutService;

    Executor statusExecutor;

    @Scheduled(cron = "${mts-adapter.jobs.check-transfers-status.cron}")
    @SchedulerLock(name = CHECK_TRANSFERS_STATUS_JOB)
    public void checkTransfersStatus() {
        log.info("Джоба '{}' запущена", CHECK_TRANSFERS_STATUS_JOB);
        long startedAt = System.currentTimeMillis();

        List<Transfers> awaiting = transfersService.findAwaitingStatus();
        AtomicInteger updated = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        List<CompletableFuture<Void>> futures = awaiting.stream()
                .map(transfer ->
                        CompletableFuture.runAsync(() -> checkStatus(transfer, updated, failed), statusExecutor)
                )
                .toList();

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        double durationSeconds = (System.currentTimeMillis() - startedAt) / 1000.0;
        log.info("Джоба '{}' завершена: обновлено: {}, ошибок: {}, время выполнения: {} сек", CHECK_TRANSFERS_STATUS_JOB, updated.get(), failed.get(), durationSeconds);
    }

    private void checkStatus(Transfers transfer, AtomicInteger updated, AtomicInteger failed) {
        try {
            TransferStatusResponseDto status = payoutService.getStatus(transfer.getCoinpayTransferId());
            if (applyStatus(transfer, status)) {
                transfersService.update(transfer);
                updated.incrementAndGet();
            }
        } catch (Exception e) {
            log.warn("Ошибка опроса статуса перевода {} (coinpayId: {}): {}", transfer.getId(), transfer.getCoinpayTransferId(), e.getMessage(), e);
            failed.incrementAndGet();
        }
    }

    /**
     * Применяет статус CoinPay к переводу. Возвращает true, если статус финальный и запись изменена.
     */
    private boolean applyStatus(Transfers transfer, TransferStatusResponseDto status) {
        CoinPayTransferStatus providerStatus = status.status();

        if (providerStatus == CoinPayTransferStatus.COMPLETED) {
            transfer.setTransactionStatus(TransactionStatus.PROCESSED);
            transfer.setInternalStatus(InternalStatus.COMPLETED);
            transfer.setInternalMessage(status.description());
            return true;
        }
        if (providerStatus == CoinPayTransferStatus.REJECTED) {
            transfer.setTransactionStatus(TransactionStatus.REJECTED);
            transfer.setInternalStatus(InternalStatus.REJECTED);
            transfer.setStatusMessage(StatusMessages.PAYMENT_ERROR);
            transfer.setInternalMessage(buildRejectMessage(status));
            return true;
        }
        return false;
    }

    private String buildRejectMessage(TransferStatusResponseDto status) {
        if (status.reasonCode() == null && status.description() == null) {
            return null;
        }
        String message = String.join(" - ",
                status.reasonCode() != null ? status.reasonCode() : "",
                status.description() != null ? status.description() : "");

        return message.length() > MAX_INTERNAL_MESSAGE_LENGTH
                ? message.substring(0, MAX_INTERNAL_MESSAGE_LENGTH)
                : message;
    }
}
