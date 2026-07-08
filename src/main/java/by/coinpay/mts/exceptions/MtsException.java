package by.coinpay.mts.exceptions;

import by.coinpay.mts.enums.MtsError;
import lombok.Getter;

/** Доменное исключение MTS. Несёт {@link MtsError} и опциональную детализацию. */
@Getter
public class MtsException extends RuntimeException {

    private final MtsError error;
    private final String details;

    public MtsException(MtsError error, String details) {
        super(error.getMessage());
        this.error = error;
        this.details = details;
    }
}
