package by.coinpay.mts.models.dto.mts.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AmountDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal amount,
        String currencyCode
) {
}
