package by.coinpay.mts.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Логирует входящие HTTP-запросы и исходящие ответы.
 */
@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {       //todo посмотреть как будет работать с logback и в vector

    private static final String REQUEST_ARROW = ">>";
    private static final String RESPONSE_ARROW = "<<";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        CachedBodyRequestWrapper wrappedRequest = new CachedBodyRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        logRequest(wrappedRequest);
        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logResponse(wrappedRequest, wrappedResponse);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(CachedBodyRequestWrapper request) {
        String query = request.getQueryString();
        String uri = request.getRequestURL() + (query != null ? "?" + query : "");
        String body = new String(request.getCachedBody(), StandardCharsets.UTF_8);

        log.info("""

                        {a} URI: {}
                        {a} Method: {}
                        {a} Headers: {}
                        {a} Body: {}""".replace("{a}", REQUEST_ARROW),
                uri, request.getMethod(), requestHeaders(request), body);
    }

    private void logResponse(CachedBodyRequestWrapper request, ContentCachingResponseWrapper response) {
        String query = request.getQueryString();
        String uri = request.getRequestURL() + (query != null ? "?" + query : "");
        String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("""

                        {a} URI: {}
                        {a} Method: {}
                        {a} Status: {}
                        {a} Headers: {}
                        {a} Body: {}""".replace("{a}", RESPONSE_ARROW),
                uri, request.getMethod(), response.getStatus(), responseHeaders(response), body);
    }

    private Map<String, Object> requestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new LinkedHashMap<>();
        Collections.list(request.getHeaderNames())
                .forEach(name -> headers.put(name, Collections.list(request.getHeaders(name))));
        return headers;
    }

    private Map<String, Object> responseHeaders(HttpServletResponse response) {
        Map<String, Object> headers = new LinkedHashMap<>();
        response.getHeaderNames().forEach(name -> headers.put(name, response.getHeaders(name)));
        return headers;
    }

    /**
     * Обёртка, вычитывающая тело запроса заранее, чтобы его можно было залогировать до контроллера.
     */
    private static class CachedBodyRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] cachedBody;

        CachedBodyRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
        }

        byte[] getCachedBody() {
            return cachedBody;
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream source = new ByteArrayInputStream(cachedBody);
            return new ServletInputStream() {
                @Override
                public int read() {
                    return source.read();
                }

                @Override
                public boolean isFinished() {
                    return source.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    // Синхронное чтение, слушатель не требуется.
                }
            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }
}
