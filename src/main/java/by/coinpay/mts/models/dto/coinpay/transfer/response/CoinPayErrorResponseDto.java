package by.coinpay.mts.models.dto.coinpay.transfer.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Тело ошибки CoinPay Transfer API: {@code { "code", "message", "traceId" }}. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinPayErrorResponseDto(
        String code,
        String message,
        String traceId
) {
}
