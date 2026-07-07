package by.coinpay.mts.models.dto.mts.registry;

import by.coinpay.mts.models.dto.mts.ErrorDto;
import by.coinpay.mts.models.dto.mts.common.MoneyDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MtsRegistryResponseDto(
        List<Transaction> transactions,
        ErrorDto error
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Transaction(
            UUID transactionId,
            String recordType,
            UUID serviceId,
            String externalTransactionNum,
            String transactionStatus,
            String transactionDate,
            String countryCode,
            MoneyDto money
    ) {
    }
}
