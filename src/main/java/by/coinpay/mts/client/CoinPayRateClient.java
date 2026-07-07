package by.coinpay.mts.client;

import by.coinpay.mts.models.dto.coinpay.rates.response.CoinPayRatesResponse;
import by.coinpay.mts.config.CacheConfig;
import by.coinpay.mts.config.CoinPayRateClientConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "coinpay-rate-api",
        url = "${mts-adapter.rest.coinpay-rate.url}",
        configuration = CoinPayRateClientConfig.class)
public interface CoinPayRateClient {

    @Cacheable(cacheNames = CacheConfig.COINPAY_RATES, key = "#baseCurrency + '_' + #quoteCurrency + '_' + #partnerId")
    @GetMapping("/rates")
    CoinPayRatesResponse getRatesV2(
            @RequestParam("baseCurrency") String baseCurrency,
            @RequestParam("quoteCurrency") String quoteCurrency,
            @RequestParam("partnerId") Long partnerId
    );
}
