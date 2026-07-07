package by.coinpay.mts.models.dto.mts;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDto(
        Integer code,
        String message,
        String externalCode,
        String details,
        UUID uuid
) {

    public static ErrorDto success() {
        return new ErrorDto(0, null, null, null, UUID.randomUUID());
    }
}
