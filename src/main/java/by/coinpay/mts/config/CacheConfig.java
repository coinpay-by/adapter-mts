package by.coinpay.mts.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String COINPAY_RATES = "coinpayRates";

    @Value("${mts-adapter.cache.coinpay-rates-ttl-minutes}")
    private long ratesTtlMinutes;

    private static final long MAXIMUM_CACHE_SIZE_RATES = 1000L;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(COINPAY_RATES);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                        .expireAfterWrite(ratesTtlMinutes, TimeUnit.MINUTES)
                        .maximumSize(MAXIMUM_CACHE_SIZE_RATES)
        );
        return cacheManager;
    }
}
