package by.coinpay.mts.service;

import by.coinpay.mts.client.CoinPayRateClient;
import by.coinpay.mts.config.property.CoinPayRateProperties;
import by.coinpay.mts.config.property.RateProperty;
import by.coinpay.mts.enums.MtsError;
import by.coinpay.mts.exceptions.ExternalException;
import by.coinpay.mts.exceptions.RatesException;
import by.coinpay.mts.mapper.RateMapper;
import by.coinpay.mts.models.dto.mts.rates.request.MtsRateRequestDto;
import by.coinpay.mts.models.dto.coinpay.rates.CoinPayRatesResponse;
import by.coinpay.mts.models.dto.mts.rates.response.MtsRateResponseDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateService {

    CoinPayRateClient coinPayRateClient;

    RateMapper rateMapper;

    CoinPayRateProperties coinPayRateProperties;
    RateProperty rateProperty;

    public MtsRateResponseDto getRate(MtsRateRequestDto request) {
        if (rateProperty.isHardRateEnable()) {
            log.info("Hard rate flag enabled, returning {}", rateProperty.getHardRateValue());
            return rateMapper.toResponse(request, rateProperty.getHardRateValue());
        }

        CoinPayRatesResponse rates = fetchRates(request);

        validateRateInRange(rates.convertedRate());

        return rateMapper.toResponse(request, rates);
    }

    private CoinPayRatesResponse fetchRates(MtsRateRequestDto request) {
        try {
            return coinPayRateClient.getRatesV2(
                    request.currencyFrom(),
                    request.currencyTo(),
                    coinPayRateProperties.getMtsPartnerId());
        } catch (Exception e) {
            log.warn("Failed to fetch rate from CoinPay: {} -> {}: {}", request.currencyFrom(), request.currencyTo(), e.getMessage(), e);
            throw new ExternalException(MtsError.UNEXPECTED_ERROR, "Ошибка при получении курсов");
        }
    }

    private void validateRateInRange(BigDecimal rate) {
        BigDecimal lowerBound = rateProperty.getLowerBound();
        BigDecimal upperBound = rateProperty.getUpperBound();

        if (rate.compareTo(lowerBound) <= 0 || rate.compareTo(upperBound) >= 0) {
            log.warn("CoinPay rate is out of allowed range. rate={}, lowerBound={}, upperBound={}", rate, lowerBound, upperBound);
            throw new RatesException(MtsError.OTHER, "Ошибка при получении курса");
        }
    }
}
