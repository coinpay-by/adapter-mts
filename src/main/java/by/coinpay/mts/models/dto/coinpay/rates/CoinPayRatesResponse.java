package by.coinpay.mts.models.dto.coinpay.rates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinPayRatesResponse(
        boolean ok,
        String baseCurrency,
        String quoteCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedRate,
        BigDecimal convertedAmount
) {
}
