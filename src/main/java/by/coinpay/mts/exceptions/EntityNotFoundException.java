package by.coinpay.mts.exceptions;

import by.coinpay.mts.enums.MtsError;

public class EntityNotFoundException extends MtsException {

    private static final String DETAILS_TEMPLATE = "Операция с transactionId: %s не найдена";

    public EntityNotFoundException(String transactionId) {
        super(MtsError.ENTITY_NOT_FOUND, DETAILS_TEMPLATE.formatted(transactionId));
    }
}
