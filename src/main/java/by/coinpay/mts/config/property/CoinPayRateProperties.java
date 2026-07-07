package by.coinpay.mts.config.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mts-adapter.params.coinpay-rate")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoinPayRateProperties {
    /** Bearer-токен авторизации. */
    String token;
    /** Идентификатор МТС в CoinPay */
    Long mtsPartnerId;
}
