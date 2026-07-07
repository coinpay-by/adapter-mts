package by.coinpay.mts.models.dto.mts.response;

import by.coinpay.mts.models.dto.mts.ErrorDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MtsRegistryResponseDto(
        List<RegistryTransactionDto> transactions,
        ErrorDto error
) {
}
