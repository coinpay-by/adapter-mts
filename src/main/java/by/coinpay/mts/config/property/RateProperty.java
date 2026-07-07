package by.coinpay.mts.config.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@Setter
@ConfigurationProperties(prefix = "mts-adapter.params.rate")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateProperty {
    /**
     * Флаг, при котором будем отдавать фиксированный курс
     */
    boolean hardRateEnable;
    /**
     * Фиксированный курс
     */
    BigDecimal hardRateValue;
    /**
     * Нижняя граница допустимого курса
     */
    BigDecimal lowerBound;
    /**
     * Верхняя граница допустимого курса
     */
    BigDecimal upperBound;
}
