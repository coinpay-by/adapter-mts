package by.coinpay.mts.models.dto.mts.status;

import by.coinpay.mts.models.dto.mts.ErrorDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MtsStatusResponseDto(
        UUID transactionId,
        String transactionState,
        String reason,
        String paymentDateUpdated,
        ErrorDto error
) {
}
