package by.coinpay.mts.mapper;

import by.coinpay.mts.config.CommonMapperConfig;
import by.coinpay.mts.enums.TransactionStatus;
import by.coinpay.mts.models.dto.mts.ErrorDto;
import by.coinpay.mts.models.dto.mts.common.AmountDto;
import by.coinpay.mts.models.dto.mts.common.MoneyDto;
import by.coinpay.mts.models.dto.mts.transfer.MtsCreateTransactionRequestDto;
import by.coinpay.mts.models.dto.mts.transfer.MtsCreateTransactionResponseDto;
import by.coinpay.mts.models.dto.mts.status.MtsStatusResponseDto;
import by.coinpay.mts.models.dto.mts.registry.MtsRegistryResponseDto;
import by.coinpay.mts.models.entity.Transfers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;

@Mapper(
        config = CommonMapperConfig.class,
        imports = {ErrorDto.class, TransactionStatus.class, OffsetDateTime.class})
public interface TransfersMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "recordType", source = "recordType")
    @Mapping(target = "serviceId", source = "serviceId")
    @Mapping(target = "transactionStatus", expression = "java(TransactionStatus.CREATED)")
    @Mapping(target = "internalStatus", ignore = true)
    @Mapping(target = "countryCode", source = "countryCode")
    @Mapping(target = "accountNumber", source = "transaction.beneficiaryAccountNumber")
    @Mapping(target = "senderComment", source = "transaction.senderComment")
    @Mapping(target = "settlementAmount", source = "money.settlementMoney.amount")
    @Mapping(target = "settlementCurrency", source = "money.settlementMoney.currencyCode")
    @Mapping(target = "withdrawAmount", source = "money.withdrawMoney.amount")
    @Mapping(target = "withdrawCurrency", source = "money.withdrawMoney.currencyCode")
    @Mapping(target = "feeAmount", source = "money.fee.amount")
    @Mapping(target = "feeCurrency", source = "money.fee.currencyCode")
    @Mapping(target = "rate", source = "money.rate")
    @Mapping(target = "transactionDate", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "firstName", source = "beneficiary.firstName")
    @Mapping(target = "lastName", source = "beneficiary.lastName")
    @Mapping(target = "middleName", source = "beneficiary.middleName")
    @Mapping(target = "birthDate", source = "beneficiary.birthDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "phone", source = "beneficiary.phoneNumber")
    @Mapping(target = "senderFirstName", source = "sender.firstName")
    @Mapping(target = "senderLastName", source = "sender.lastName")
    @Mapping(target = "senderMiddleName", source = "sender.middleName")
    @Mapping(target = "senderBirthDate", source = "sender.birthDate")
    @Mapping(target = "senderFullAddress", source = "sender.addresses.registrationAddress.full")
    @Mapping(target = "senderCountry", source = "sender.countryOfResidence")
    @Mapping(target = "senderState", source = "sender.addresses.registrationAddress.region")
    @Mapping(target = "senderCity", source = "sender.addresses.registrationAddress.city")
    @Mapping(target = "senderStreet", source = "sender.addresses.registrationAddress.street")
    @Mapping(target = "senderZip", source = "sender.addresses.registrationAddress.zipCode")
    @Mapping(target = "coinpayTransferId", ignore = true)
    @Mapping(target = "statusMessage", ignore = true)
    @Mapping(target = "internalMessage", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Transfers toEntity(MtsCreateTransactionRequestDto request);

    @Mapping(target = "transactionId", source = "transfer.transactionId")
    @Mapping(target = "recordType", source = "transfer.recordType")
    @Mapping(target = "serviceId", source = "transfer.serviceId")
    @Mapping(target = "externalTransactionNum", expression = "java(transfer.getId() != null ? transfer.getId().toString() : null)")
    @Mapping(target = "transactionStatus", expression = "java(transfer.getTransactionStatus().name())")
    @Mapping(target = "transactionDate", expression = "java(transfer.getTransactionDate() != null ? transfer.getTransactionDate().toString() : null)")
    @Mapping(target = "countryCode", source = "transfer.countryCode")
    @Mapping(target = "beneficiaryAccountNumber", source = "transfer.accountNumber")
    @Mapping(target = "beneficiaryPhoneNumber", source = "transfer.phone")
    @Mapping(target = "senderComment", source = "transfer.senderComment")
    @Mapping(target = "beneficiary", expression = "java(toBeneficiary(transfer))")
    @Mapping(target = "money", expression = "java(toMoney(transfer))")
    @Mapping(target = "error", expression = "java(ErrorDto.success())")
    MtsCreateTransactionResponseDto toResponse(Transfers transfer);

    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "transactionState", expression = "java(transfer.getTransactionStatus().name())")
    @Mapping(target = "reason", source = "statusMessage")
    @Mapping(target = "paymentDateUpdated", expression = "java(transfer.getUpdatedAt() != null ? transfer.getUpdatedAt().toString() : null)")
    @Mapping(target = "error", expression = "java(ErrorDto.success())")
    MtsStatusResponseDto toStatusResponse(Transfers transfer);

    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "recordType", source = "recordType")
    @Mapping(target = "serviceId", source = "serviceId")
    @Mapping(target = "externalTransactionNum", expression = "java(transfer.getId() != null ? transfer.getId().toString() : null)")
    @Mapping(target = "transactionStatus", expression = "java(transfer.getTransactionStatus().name())")
    @Mapping(target = "transactionDate", expression = "java(transfer.getTransactionDate() != null ? transfer.getTransactionDate().toString() : null)")
    @Mapping(target = "countryCode", source = "countryCode")
    @Mapping(target = "money", expression = "java(toMoney(transfer))")
    MtsRegistryResponseDto.Transaction toRegistryTransaction(Transfers transfer);

    default MoneyDto toMoney(Transfers transfer) {
        return new MoneyDto(
                new AmountDto(transfer.getSettlementAmount(), transfer.getSettlementCurrency()),
                new AmountDto(transfer.getWithdrawAmount(), transfer.getWithdrawCurrency()),
                new AmountDto(transfer.getFeeAmount(), transfer.getFeeCurrency()),
                transfer.getRate()
        );
    }

    default MtsCreateTransactionResponseDto.Beneficiary toBeneficiary(Transfers transfer) {
        return new MtsCreateTransactionResponseDto.Beneficiary(
                transfer.getLastName(),
                transfer.getFirstName(),
                transfer.getMiddleName(),
                transfer.getPhone(),
                null
        );
    }
}
