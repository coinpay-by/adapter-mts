package by.coinpay.mts.models.dto.mts.transfer.response;

import by.coinpay.mts.models.dto.mts.ErrorDto;
import by.coinpay.mts.models.dto.mts.common.JuridicalDto;
import by.coinpay.mts.models.dto.mts.common.MoneyDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MtsCreateTransactionResponseDto(
        UUID transactionId,
        String recordType,
        UUID serviceId,
        String externalTransactionNum,
        String transactionStatus,
        String transactionDate,
        String countryCode,
        String beneficiaryAccountNumber,
        String beneficiaryPhoneNumber,
        String senderComment,
        Beneficiary beneficiary,
        MoneyDto money,
        ErrorDto error
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Beneficiary(
            String lastName,
            String firstName,
            String middleName,
            String phoneNumber,
            JuridicalDto juridical
    ) {
    }
}
