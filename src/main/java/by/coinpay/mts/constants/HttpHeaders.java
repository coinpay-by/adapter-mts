package by.coinpay.mts.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpHeaders {

    public static final String BEARER = "Bearer ";

    public static final String X_TERMINAL_ID = "X-Terminal-Id";
    public static final String X_TIMESTAMP = "X-Timestamp";
    public static final String X_SIGNATURE = "X-Signature";

    public static final String X_FORWARD_FOR = "X-Forwarded-For";
}
