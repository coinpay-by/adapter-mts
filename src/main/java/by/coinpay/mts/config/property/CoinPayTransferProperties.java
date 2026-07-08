package by.coinpay.mts.config.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mts-adapter.params.coinpay-transfer")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoinPayTransferProperties {
    /** UUID терминала (X-Terminal-Id). */
    String terminalId;
    /** Секрет терминала для HMAC-подписи. */
    String secret;
    /** Окно исполнения перевода (executionDeadline), минуты. */
    int executionDeadlineMinutes;
}
