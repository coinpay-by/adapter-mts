package by.coinpay.mts.exceptions;

import by.coinpay.mts.enums.MtsError;

public class ExternalException extends MtsException {

    public ExternalException(MtsError error, String details) {
        super(error, details);
    }
}
