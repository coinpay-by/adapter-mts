package by.coinpay.mts.repository;

import by.coinpay.mts.enums.InternalStatus;
import by.coinpay.mts.enums.TransactionStatus;
import by.coinpay.mts.models.entity.Transfers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransfersRepository extends JpaRepository<Transfers, UUID> {

    Optional<Transfers> findByTransactionId(UUID transactionId);

    boolean existsByTransactionId(UUID transactionId);

    @Modifying(clearAutomatically = true)
    @Query("""
            update Transfers t
               set t.transactionStatus = :toStatus,
                   t.updatedAt = CURRENT_TIMESTAMP
             where t.transactionStatus = :fromStatus
               and t.createdAt < :threshold
            """)
    int updateStatusForExpired(
            @Param("fromStatus") TransactionStatus fromStatus,
            @Param("toStatus") TransactionStatus toStatus,
            @Param("threshold") OffsetDateTime threshold);

    List<Transfers> findByTransactionStatusAndInternalStatusIsNull(TransactionStatus transactionStatus);

    List<Transfers> findByTransactionStatusAndInternalStatus(TransactionStatus transactionStatus, InternalStatus internalStatus);

    List<Transfers> findByTransactionDateGreaterThanEqualAndTransactionDateLessThan(OffsetDateTime from, OffsetDateTime to);
}
