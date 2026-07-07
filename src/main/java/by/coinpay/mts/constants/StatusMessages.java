package by.coinpay.mts.constants;

import lombok.experimental.UtilityClass;

/**
 * Тексты статуса для MTS (поле status_message, передается в /status).
 */
@UtilityClass
public class StatusMessages {
    public final String PAYMENT_ERROR = "Ошибка при оплате";
}
