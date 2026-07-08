package by.coinpay.mts.config;

import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Логирует исходящие Feign-вызовы.
 */
@Slf4j
public class FeignLogger extends feign.Logger {     //todo посмотреть как будет работать с logback и в vector

    private static final String REQUEST_ARROW = ">";
    private static final String RESPONSE_ARROW = "<";

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        String body = request.body() != null
                ? new String(request.body(), StandardCharsets.UTF_8)
                : "";

        log.info("""

                        {a} URI: {}
                        {a} Method: {}
                        {a} Headers: {}
                        {a} Body: {}""".replace("{a}", REQUEST_ARROW),
                request.url(), request.httpMethod(), request.headers(), body);
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime)
            throws IOException {
        String body = "";
        byte[] bodyData = null;
        if (response.body() != null) {
            bodyData = Util.toByteArray(response.body().asInputStream());
            body = new String(bodyData, StandardCharsets.UTF_8);
        }

        log.info("""

                        {a} URI: {}
                        {a} Status: {}
                        {a} Elapsed: {} ms
                        {a} Headers: {}
                        {a} Body: {}""".replace("{a}", RESPONSE_ARROW),
                response.request().url(), response.status(), elapsedTime, response.headers(), body);

        if (bodyData != null) {
            return response.toBuilder().body(bodyData).build();
        }
        return response;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        // Логирование выполняется целиком в logRequest / logAndRebufferResponse.
    }
}
