package by.coinpay.mts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {

    private static final String PAYOUT_EXECUTOR = "payoutExecutor";
    private static final int PAYOUT_CORE_POOL_SIZE = 10;
    private static final int PAYOUT_MAX_POOL_SIZE = 20;
    private static final int PAYOUT_QUEUE_CAPACITY = 100;

    private static final String STATUS_EXECUTOR = "statusExecutor";
    private static final int STATUS_CORE_POOL_SIZE = 10;
    private static final int STATUS_MAX_POOL_SIZE = 20;
    private static final int STATUS_QUEUE_CAPACITY = 100;

    @Bean(PAYOUT_EXECUTOR)
    public Executor payoutExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(PAYOUT_CORE_POOL_SIZE);
        executor.setMaxPoolSize(PAYOUT_MAX_POOL_SIZE);
        executor.setQueueCapacity(PAYOUT_QUEUE_CAPACITY);
        executor.setThreadNamePrefix("payout-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean(STATUS_EXECUTOR)
    public Executor statusExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(STATUS_CORE_POOL_SIZE);
        executor.setMaxPoolSize(STATUS_MAX_POOL_SIZE);
        executor.setQueueCapacity(STATUS_QUEUE_CAPACITY);
        executor.setThreadNamePrefix("status-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
