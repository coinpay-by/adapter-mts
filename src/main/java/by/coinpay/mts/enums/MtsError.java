package by.coinpay.mts.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/** Стандартные ошибки Multitransfer (раздел 2.4 спецификации). */
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MtsError {

    UNEXPECTED_ERROR(100, "Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR),
    INCORRECT_REQUEST(101, "Incorrect request", HttpStatus.BAD_REQUEST),
    INVALID_BACKEND_RESPONSE(102, "Invalid backend response", HttpStatus.BAD_GATEWAY),
    BACKEND_TIMEOUT(103, "Backend timeout", HttpStatus.GATEWAY_TIMEOUT),
    INTERNAL_TIMEOUT(106, "Internal timeout", HttpStatus.SERVICE_UNAVAILABLE),
    ENTITY_NOT_FOUND(108, "Entity not found", HttpStatus.NOT_FOUND),
    OPERATION_CANNOT_BE_PROCESSED(110, "Operation can not be processed", HttpStatus.NOT_ACCEPTABLE),
    ACCESS_DENIED(122, "Access denied", HttpStatus.FORBIDDEN),
    PROCESSING(123, "Processing", HttpStatus.NOT_ACCEPTABLE),
    ALREADY_EXISTS(126, "Запись с такими параметрами уже существует", HttpStatus.FORBIDDEN),
    SENDER_KYC_FAILED(131, "В досье отправителя не прошло KYC", HttpStatus.NOT_ACCEPTABLE),
    RECEIVER_KYC_FAILED(132, "В досье получателя не прошло KYC", HttpStatus.NOT_ACCEPTABLE),
    AMOUNT_OUT_OF_RANGE(133, "Сумма выдачи не попадает в диапазон", HttpStatus.NOT_ACCEPTABLE),
    INVALID_CURRENCY(134, "Некорректная валюта выдачи", HttpStatus.NOT_ACCEPTABLE),
    OTHER(999, "Прочие ошибки", HttpStatus.NOT_ACCEPTABLE);

    int code;
    String message;
    HttpStatus httpStatus;
}
