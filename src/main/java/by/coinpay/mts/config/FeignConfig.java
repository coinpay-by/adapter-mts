package by.coinpay.mts.config;

import by.coinpay.mts.config.property.FeignRetryProperties;
import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    public Retryer defaultRetryer(FeignRetryProperties props) {
        return new Retryer.Default(props.getPeriod(), props.getMaxPeriod(), props.getMaxAttempts());
    }

    @Bean
    public Logger feignLogger() {
        return new FeignLogger();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
