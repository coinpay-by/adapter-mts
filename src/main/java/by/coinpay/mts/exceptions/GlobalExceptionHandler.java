package by.coinpay.mts.exceptions;

import by.coinpay.mts.enums.MtsError;
import by.coinpay.mts.models.dto.mts.ErrorDto;
import by.coinpay.mts.models.dto.mts.MtsErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;
import java.util.stream.Collectors;

import static by.coinpay.mts.enums.MtsError.*;

/** Единая обработка ошибок: ответ в формате MTS {@code { "error": {...} }}. */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MtsException.class)
    public ResponseEntity<MtsErrorResponse> handleMtsException(MtsException ex) {
        return build(ex.getError(), ex.getDetails());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MtsErrorResponse> handleException(Exception ex) {
        log.warn("Unexpected error", ex);
        return build(UNEXPECTED_ERROR, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MtsErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                        .map(f -> f.getField() + ": " + f.getDefaultMessage())
                        .collect(Collectors.joining("; "));
        log.warn("Request validation error: {}", details);
        return build(INCORRECT_REQUEST, details);
    }

    private ResponseEntity<MtsErrorResponse> build(MtsError error, String details) {
        ErrorDto errorDto = new ErrorDto(error.getCode(), error.getMessage(), null, details, UUID.randomUUID());
        return ResponseEntity.status(error.getHttpStatus()).body(new MtsErrorResponse(errorDto));
    }
}
