package by.coinpay.mts.models.dto.mts.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record JuridicalDto(
        String bankId,
        String bankName,
        String legalMerchantName,
        String mccCode,
        String merchantAddress,
        String merchantName,
        String paymentPurpose
) {
}
