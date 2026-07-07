package by.coinpay.mts.enums;

/** Внутренние статусы операции (этап взаимодействия с CoinPay payout-API). */
public enum InternalStatus {
    /** Отправлено в CoinPay, ждём финального статуса. */
    SENT,
    /** CoinPay завершил операцию успешно. */
    COMPLETED,
    /** Отклонено CoinPay. */
    REJECTED,
    /** Ошибка при отправке в CoinPay (сетевой сбой / некорректный ответ). */
    SEND_FAILED
}
