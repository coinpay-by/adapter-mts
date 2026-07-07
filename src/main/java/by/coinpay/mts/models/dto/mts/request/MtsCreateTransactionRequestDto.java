package by.coinpay.mts.models.dto.mts.request;

import by.coinpay.mts.models.dto.mts.common.MoneyDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MtsCreateTransactionRequestDto(
        @NotNull UUID transactionId,
        @NotBlank String recordType,
        UUID serviceId,
        @NotBlank @Size(min = 3, max = 3) String countryCode,
        @NotNull @Valid MoneyDto money,
        @NotNull @Valid TransactionDetailsDto transaction,
        @Valid SenderDto sender,
        @Valid BeneficiaryDto beneficiary
) {
}
