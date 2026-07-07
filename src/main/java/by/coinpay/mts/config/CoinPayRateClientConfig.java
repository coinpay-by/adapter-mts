package by.coinpay.mts.config;

import by.coinpay.mts.config.property.CoinPayRateProperties;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import static by.coinpay.mts.constants.HttpHeaders.BEARER;
import static org.springframework.http.HttpHeaders.*;

/**
 * Конфигурация Feign-клиента CoinPay. Retryer и таймауты наследуются от общего {@link FeignConfig} /
 * application.yml.
 */
public class CoinPayRateClientConfig {

    @Bean
    public RequestInterceptor coinPayBearerInterceptor(CoinPayRateProperties properties) {
        return template -> template.header(AUTHORIZATION, BEARER + properties.getToken());
    }
}
