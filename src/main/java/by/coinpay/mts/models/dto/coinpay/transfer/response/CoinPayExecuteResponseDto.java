package by.coinpay.mts.models.dto.coinpay.transfer.response;

import by.coinpay.mts.enums.CoinPayTransferStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinPayExecuteResponseDto(
        UUID id,
        String externalId,
        CoinPayTransferStatus status,
        String reasonCode,
        String executionDeadline,
        String createdAt,
        String updatedAt
) {
}
