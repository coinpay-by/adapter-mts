package by.coinpay.mts.models.dto.mts.rates.response;

import by.coinpay.mts.models.dto.mts.ErrorDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Ответ метода GET /rate.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MtsRateResponseDto(
        UUID serviceId,
        String currencyFrom,
        String currencyTo,
        BigDecimal amountFrom,
        BigDecimal amountTo,
        BigDecimal exchangeRate,
        Integer multy,
        BigDecimal sellRate,
        BigDecimal buyRate,
        ErrorDto error
) {
}
