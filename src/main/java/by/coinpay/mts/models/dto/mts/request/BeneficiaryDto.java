package by.coinpay.mts.models.dto.mts.request;

import by.coinpay.mts.models.dto.mts.common.AddressesDto;
import by.coinpay.mts.models.dto.mts.common.DocumentDto;
import by.coinpay.mts.models.dto.mts.common.JuridicalDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BeneficiaryDto(
        String lastName,
        String firstName,
        String middleName,
        String gender,
        String countryOfResidence,
        String citizenship,
        String birthDate,
        String phoneNumber,
        List<DocumentDto> documents,
        AddressesDto addresses,
        JuridicalDto juridical
) {
}
