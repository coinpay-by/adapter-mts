package by.coinpay.mts.models.dto.mts.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record DocumentDto(
        String type,
        String countryCode,
        String series,
        String number,
        String issuer,
        String issuerCode,
        String issueDate,
        String expiryDate
) {
}
