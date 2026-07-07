package by.coinpay.mts.models.dto.mts.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneyDto(
        @Valid AmountDto settlementMoney,
        @Valid @NotNull AmountDto withdrawMoney,
        @Valid AmountDto fee,
        @NotNull BigDecimal rate
) {
}
