package by.coinpay.mts.models.dto.coinpay.transfer.response;

import by.coinpay.mts.enums.CoinPayTransferStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransferStatusResponseDto(
        UUID id,
        CoinPayTransferStatus status,
        String reasonCode,
        String description,
        String executionDeadline,
        String updatedAt
) {
}
