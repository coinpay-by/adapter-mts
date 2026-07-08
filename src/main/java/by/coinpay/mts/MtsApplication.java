package by.coinpay.mts;

import by.coinpay.mts.config.FeignConfig;
import by.coinpay.mts.config.property.CoinPayRateProperties;
import by.coinpay.mts.config.property.FeignRetryProperties;
import by.coinpay.mts.config.property.RateProperty;
import by.coinpay.mts.config.property.CoinPayTransferProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(defaultConfiguration = FeignConfig.class)
@EnableConfigurationProperties({
    FeignRetryProperties.class,
    CoinPayRateProperties.class,
    RateProperty.class,
    CoinPayTransferProperties.class
})
public class MtsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MtsApplication.class, args);
    }
}
//todo заголовки и mdc context x-request-id

