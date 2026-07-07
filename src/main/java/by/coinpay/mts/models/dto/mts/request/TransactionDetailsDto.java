package by.coinpay.mts.models.dto.mts.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionDetailsDto(
        String beneficiaryAccountNumber,
        String beneficiaryPhoneNumber,
        String beneficiaryIdentityData,
        UUID qrTicketId,
        String senderComment
) {
}
