package by.coinpay.mts.config;

import by.coinpay.mts.config.property.CoinPayTransferProperties;
import feign.RequestInterceptor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static by.coinpay.mts.constants.HttpHeaders.X_SIGNATURE;
import static by.coinpay.mts.constants.HttpHeaders.X_TERMINAL_ID;
import static by.coinpay.mts.constants.HttpHeaders.X_TIMESTAMP;

/**
 * Конфигурация Feign-клиента CoinPay Transfer API.
 *
 * <p>Подписывает каждый запрос HMAC-SHA256 по канон-строке {@code
 * METHOD\nPATH\nX-Timestamp\nhex(SHA-256(body))} и проставляет заголовки X-Terminal-Id / X-Timestamp
 * / X-Signature. Retryer и таймауты наследуются от общего {@link FeignConfig} / application.yml.
 */
public class CoinPayTransferClientConfig {

    @Bean
    public RequestInterceptor transferApiHmacInterceptor(CoinPayTransferProperties properties) {
        return template -> {
            String timestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();

            byte[] body = template.body() != null ? template.body() : new byte[0];
            String bodyHash = DigestUtils.sha256Hex(body);

            String canonical = String.join("\n", template.method(), template.path(), timestamp, bodyHash);

            String signature = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, properties.getSecret()).hmacHex(canonical);

            template.header(X_TERMINAL_ID, properties.getTerminalId());
            template.header(X_TIMESTAMP, timestamp);
            template.header(X_SIGNATURE, signature);
        };
    }
}
