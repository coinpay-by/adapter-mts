package by.coinpay.mts.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static by.coinpay.mts.constants.HttpHeaders.X_FORWARD_FOR;

/**
 * Кладёт IP клиента в MDC под ключом {@code ip} на время обработки запроса, чтобы он попадал в логи
 * (паттерн {@code [ip=%X{ip:-}]}). Берёт X-Forwarded-For, иначе — адрес соединения.
 * Идёт раньше остальных фильтров, чтобы IP был доступен уже при логировании входящего запроса.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IpFilter implements Filter {

    private static final String IP_MDC_KEY = "ip";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            if (request instanceof HttpServletRequest httpRequest) {
                String ip = httpRequest.getHeader(X_FORWARD_FOR);
                if (ip == null || ip.isBlank()) {
                    ip = request.getRemoteAddr();
                }
                MDC.put(IP_MDC_KEY, ip);
            }

            chain.doFilter(request, response);
        } finally {
            MDC.remove(IP_MDC_KEY);
        }
    }
}
