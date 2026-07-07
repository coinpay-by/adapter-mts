package by.coinpay.mts.enums;

/** Публичные статусы операции в CoinPay Transfer API. */
public enum CoinPayTransferStatus {
    /** В обработке. */
    PENDING,
    /** Успешно завершён. */
    COMPLETED,
    /** Отклонён. */
    REJECTED
}
