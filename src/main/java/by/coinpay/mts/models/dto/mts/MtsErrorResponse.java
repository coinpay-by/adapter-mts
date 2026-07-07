package by.coinpay.mts.models.dto.mts;

import com.fasterxml.jackson.annotation.JsonInclude;

/** Ответ-ошибка MTS: {@code { "error": {...} }}. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MtsErrorResponse(
        ErrorDto error
) {
}
