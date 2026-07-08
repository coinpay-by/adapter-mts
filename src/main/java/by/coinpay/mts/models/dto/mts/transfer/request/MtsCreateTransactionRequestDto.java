package by.coinpay.mts.models.dto.mts.transfer.request;

import by.coinpay.mts.models.dto.mts.common.AddressesDto;
import by.coinpay.mts.models.dto.mts.common.DocumentDto;
import by.coinpay.mts.models.dto.mts.common.JuridicalDto;
import by.coinpay.mts.models.dto.mts.common.MoneyDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MtsCreateTransactionRequestDto(
        @NotNull UUID transactionId,
        @NotBlank String recordType,
        UUID serviceId,
        @NotBlank @Size(min = 3, max = 3) String countryCode,
        @NotNull @Valid MoneyDto money,
        @NotNull @Valid Transaction transaction,
        @Valid Sender sender,
        @Valid Beneficiary beneficiary
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Transaction(
            String beneficiaryAccountNumber,
            String beneficiaryPhoneNumber,
            String beneficiaryIdentityData,
            UUID qrTicketId,
            String senderComment
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Sender(
            String lastName,
            String firstName,
            String middleName,
            String gender,
            String tiin,
            String countryOfResidence,
            String citizenship,
            String birthDate,
            String phoneNumber,
            List<DocumentDto> documents,
            AddressesDto addresses,
            JuridicalDto juridical
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Beneficiary(
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
}
