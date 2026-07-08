package by.coinpay.mts.service;

import by.coinpay.mts.client.CoinPayTransferClient;
import by.coinpay.mts.models.dto.coinpay.transfer.request.CoinPayExecuteRequestDto;
import by.coinpay.mts.models.dto.coinpay.transfer.response.CoinPayExecuteResponseDto;
import by.coinpay.mts.models.dto.coinpay.transfer.response.TransferStatusResponseDto;
import by.coinpay.mts.config.property.CoinPayTransferProperties;
import by.coinpay.mts.models.entity.Transfers;
import by.coinpay.mts.utils.CountryCodeConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Взаимодействие с CoinPay Transfer API (payout).
 *
 * <p>Собирает из сущности {@link Transfers} тело запроса и отправляет перевод ({@code POST
 * /api/v1/transfers}), а также опрашивает его статус ({@code GET /api/v1/transfers/{id}/status}).
 * Используется фоновыми джобами отправки и опроса статусов.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PayoutService {

    CoinPayTransferClient coinPayTransferClient;
    CoinPayTransferProperties transferProperties;

    public CoinPayExecuteResponseDto send(Transfers transfer) {
        CoinPayExecuteRequestDto request = buildRequest(transfer);
        return coinPayTransferClient.createTransfer(request);
    }

    public TransferStatusResponseDto getStatus(UUID coinpayTransferId) {
        return coinPayTransferClient.getStatus(coinpayTransferId);
    }

    private CoinPayExecuteRequestDto buildRequest(Transfers transfer) {
        return new CoinPayExecuteRequestDto(
                transfer.getId().toString(),
                UUID.fromString(transferProperties.getTerminalId()),
                new CoinPayExecuteRequestDto.Money(transfer.getSettlementCurrency(), toAmount(transfer.getSettlementAmount())),
                new CoinPayExecuteRequestDto.Money(transfer.getWithdrawCurrency(), toAmount(transfer.getWithdrawAmount())),
                toRate(transfer.getRate()),
                executionDeadline(),
                transfer.getSenderComment(),
                new CoinPayExecuteRequestDto.Sender(
                        transfer.getSenderFirstName(),
                        transfer.getSenderLastName(),
                        transfer.getSenderMiddleName(),
                        toBirthDate(transfer.getSenderBirthDate()),
                        transfer.getSenderFullAddress(),
                        CountryCodeConverter.toAlpha2(transfer.getSenderCountry()),
                        transfer.getSenderState(),
                        transfer.getSenderCity(),
                        transfer.getSenderStreet(),
                        transfer.getSenderZip()
                ),
                new CoinPayExecuteRequestDto.Recipient(transfer.getAccountNumber(), holder(transfer)));
    }

    private String toAmount(BigDecimal amount) {
        return amount != null ? amount.setScale(2, RoundingMode.HALF_UP).toPlainString() : null;
    }

    private String toRate(BigDecimal rate) {
        return rate != null ? rate.toPlainString() : null;
    }

    /**
     * Момент, до которого операция может быть исполнена: UTC ISO-8601 без таймзоны.
     */
    private String executionDeadline() {
        return LocalDateTime.now(ZoneOffset.UTC)
                .plusMinutes(transferProperties.getExecutionDeadlineMinutes())
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Дата рождения отправителя в формат YYYY-MM-DD (берём дату из возможного dateTime).
     */
    private String toBirthDate(String raw) {
        if (raw == null) {
            return null;
        }
        return raw.length() >= 10 ? raw.substring(0, 10) : raw;
    }

    private String holder(Transfers transfer) {
        return String.join(
                        " ",
                        transfer.getFirstName() != null ? transfer.getFirstName() : "",
                        transfer.getLastName() != null ? transfer.getLastName() : "")
                .trim();
    }
}
