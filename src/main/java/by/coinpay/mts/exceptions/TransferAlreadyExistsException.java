package by.coinpay.mts.exceptions;

import by.coinpay.mts.enums.MtsError;

public class TransferAlreadyExistsException extends MtsException {

    private static final String DETAILS_TEMPLATE = "Перевод с transactionId: %s уже существует";

    public TransferAlreadyExistsException(String transactionId) {
        super(MtsError.ALREADY_EXISTS, DETAILS_TEMPLATE.formatted(transactionId));
    }
}
