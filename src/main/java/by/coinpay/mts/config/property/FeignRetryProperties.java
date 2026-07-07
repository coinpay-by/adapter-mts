package by.coinpay.mts.config.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Параметры общего Retryer для Feign-клиентов.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "mts-adapter.rest.retry")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeignRetryProperties {
    /**
     * Начальный интервал перед первым повтором, мс.
     */
    long period;
    /**
     * Максимальный интервал между повторами (потолок backoff), мс.
     */
    long maxPeriod;
    /**
     * Максимальное число попыток, включая первую.
     */
    int maxAttempts;
}
