package by.coinpay.mts.models.dto.mts.response;

import by.coinpay.mts.models.dto.mts.common.MoneyDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RegistryTransactionDto(
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
