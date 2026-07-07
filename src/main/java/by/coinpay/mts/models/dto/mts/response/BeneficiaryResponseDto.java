package by.coinpay.mts.models.dto.mts.response;

import by.coinpay.mts.models.dto.mts.common.JuridicalDto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BeneficiaryResponseDto(
        String lastName,
        String firstName,
        String middleName,
        String phoneNumber,
        JuridicalDto juridical
) {
}
