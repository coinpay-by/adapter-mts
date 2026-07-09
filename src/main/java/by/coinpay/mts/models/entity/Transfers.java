package by.coinpay.mts.models.entity;

import by.coinpay.mts.enums.InternalStatus;
import by.coinpay.mts.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/** Заказ Multitransfer. Поля соответствуют таблице mt_transaction. */
@Entity
@Table(name = "transfers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfers {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "transaction_id", nullable = false, unique = true, updatable = false)
    private UUID transactionId;

    @Column(name = "record_type")
    private String recordType;

    @Column(name = "service_id")
    private UUID serviceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "internal_status")
    private InternalStatus internalStatus;

    @Column(name = "internal_message")
    private String internalMessage;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "sender_comment")
    private String senderComment;

    @Column(name = "settlement_amount")
    private BigDecimal settlementAmount;

    @Column(name = "settlement_currency")
    private String settlementCurrency;

    @Column(name = "withdraw_amount")
    private BigDecimal withdrawAmount;

    @Column(name = "withdraw_currency")
    private String withdrawCurrency;

    @Column(name = "fee_amount")
    private BigDecimal feeAmount;

    @Column(name = "fee_currency")
    private String feeCurrency;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone")
    private String phone;

    @Column(name = "sender_first_name")
    private String senderFirstName;

    @Column(name = "sender_last_name")
    private String senderLastName;

    @Column(name = "sender_middle_name")
    private String senderMiddleName;

    @Column(name = "sender_birth_date")
    private String senderBirthDate;

    @Column(name = "sender_full_address")
    private String senderFullAddress;

    @Column(name = "sender_country")
    private String senderCountry;

    @Column(name = "sender_state")
    private String senderState;

    @Column(name = "sender_city")
    private String senderCity;

    @Column(name = "sender_street")
    private String senderStreet;

    @Column(name = "sender_zip")
    private String senderZip;

    @Column(name = "coinpay_transfer_id")
    private UUID coinpayTransferId;

    @Column(name = "status_message")
    private String statusMessage;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
