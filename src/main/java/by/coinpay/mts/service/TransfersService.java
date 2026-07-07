package by.coinpay.mts.service;

import by.coinpay.mts.enums.InternalStatus;
import by.coinpay.mts.enums.TransactionStatus;
import by.coinpay.mts.models.entity.Transfers;
import by.coinpay.mts.repository.TransfersRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransfersService {

    TransfersRepository transfersRepository;

    private static final Integer ONE_DAY = 1;

    public boolean existsByTransactionId(UUID transactionId) {
        return transfersRepository.existsByTransactionId(transactionId);
    }

    public Optional<Transfers> findByTransactionId(UUID transactionId) {
        return transfersRepository.findByTransactionId(transactionId);
    }

    public List<Transfers> findNotSentCoinPay(TransactionStatus status) {
        return transfersRepository.findByTransactionStatusAndInternalStatusIsNull(status);
    }

    public List<Transfers> findAwaitingStatus() {
        return transfersRepository.findByTransactionStatusAndInternalStatus(TransactionStatus.PROCESSING, InternalStatus.SENT);
    }

    public List<Transfers> findByDate(LocalDate date) {
        OffsetDateTime from = date.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime to = from.plusDays(ONE_DAY);
        return transfersRepository.findByTransactionDateGreaterThanEqualAndTransactionDateLessThan(from, to);
    }

    public Transfers save(Transfers transfer) {
        return transfersRepository.save(transfer);
    }

    public void update(Transfers transfer) {
        save(transfer);
    }

    /**
     * Отменяет переводы, зависшие в статусе CREATED, одним bulk-UPDATE.
     * @return число отмененных операций
     */
    @Transactional
    public int cancelExpiredCreated(int ttlMinutes) {
        OffsetDateTime threshold = OffsetDateTime.now().minusMinutes(ttlMinutes);
        return transfersRepository.updateStatusForExpired(TransactionStatus.CREATED, TransactionStatus.CANCELED, threshold);
    }
}
