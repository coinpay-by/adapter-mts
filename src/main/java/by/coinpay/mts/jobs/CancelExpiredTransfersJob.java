package by.coinpay.mts.jobs;

import by.coinpay.mts.service.TransfersService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Отменяет переводы, зависшие в статусе CREATED дольше TTL (30 мин по спеке MTC), переводя их в
 * CANCELED.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CancelExpiredTransfersJob {

    private static final String CANCEL_EXPIRED_TRANSFERS_JOB = "cancelExpiredTransfers";

    TransfersService transfersService;

    @Value("${mts-adapter.params.jobs-ttl.cancel-expired-ttl-minutes}")
    @NonFinal
    int expiredTtlMinutes;

    @Scheduled(cron = "${mts-adapter.jobs.cancel-expired-transfers.cron}")
    @SchedulerLock(name = CANCEL_EXPIRED_TRANSFERS_JOB)
    public void cancelExpiredTransfers() {
        log.info("Job '{}' started. Operations in CREATED status older than {} minutes will be moved to CANCELED", CANCEL_EXPIRED_TRANSFERS_JOB, expiredTtlMinutes);

        long startedAt = System.currentTimeMillis();

        int cancelled = transfersService.cancelExpiredCreated(expiredTtlMinutes);

        long durationMs = System.currentTimeMillis() - startedAt;
        double durationSeconds = durationMs / 1000.0;
        log.info("Job '{}' finished. Moved from CREATED to CANCELED: {}, duration: {} s", CANCEL_EXPIRED_TRANSFERS_JOB, cancelled, durationSeconds);
    }
}
