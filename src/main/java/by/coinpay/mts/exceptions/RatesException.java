package by.coinpay.mts.exceptions;

import by.coinpay.mts.enums.MtsError;

/** Ошибка получения/валидации курса. Обрабатывается как {@link MtsException}. */
public class RatesException extends MtsException {

    public RatesException(MtsError error, String details) {
        super(error, details);
    }
}
