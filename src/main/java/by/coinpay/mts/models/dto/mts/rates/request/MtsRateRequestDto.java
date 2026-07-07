package by.coinpay.mts.models.dto.mts.rates.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record MtsRateRequestDto(
        @NotNull(message = "serviceId required") UUID serviceId,
        @NotBlank(message = "currencyFrom required") @Size(min = 3, max = 3) String currencyFrom,
        @NotBlank(message = "currencyTo required") @Size(min = 3, max = 3) String currencyTo,
        BigDecimal amount,
        Integer multy
) {
}
