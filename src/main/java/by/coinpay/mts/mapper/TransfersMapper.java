package by.coinpay.mts.mapper;

import by.coinpay.mts.config.CommonMapperConfig;
import by.coinpay.mts.enums.TransactionStatus;
import by.coinpay.mts.models.dto.mts.ErrorDto;
import by.coinpay.mts.models.dto.mts.common.AmountDto;
import by.coinpay.mts.models.dto.mts.common.MoneyDto;
import by.coinpay.mts.models.dto.mts.transfer.request.MtsCreateTransactionRequestDto;
import by.coinpay.mts.models.dto.mts.transfer.response.MtsCreateTransactionResponseDto;
import by.coinpay.mts.models.dto.mts.status.MtsStatusResponseDto;
import by.coinpay.mts.models.dto.mts.registry.MtsRegistryResponseDto;
import by.coinpay.mts.models.entity.Transfers;
import by.coinpay.mts.utils.AddressParser;
import by.coinpay.mts.utils.AddressParser.ParsedAddress;
import by.coinpay.mts.utils.TransliterationUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.OffsetDateTime;

@Mapper(
        config = CommonMapperConfig.class,
        imports = {ErrorDto.class, TransactionStatus.class, OffsetDateTime.class})
public interface TransfersMapper {

    int MAX_ZIP_LENGTH = 20;

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
    @Mapping(target = "firstName", source = "beneficiary.firstName", qualifiedByName = "transliterate")
    @Mapping(target = "lastName", source = "beneficiary.lastName", qualifiedByName = "transliterate")
    @Mapping(target = "middleName", source = "beneficiary.middleName", qualifiedByName = "transliterate")
    @Mapping(target = "birthDate", source = "beneficiary.birthDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "phone", source = "beneficiary.phoneNumber")
    @Mapping(target = "senderFirstName", source = "sender.firstName", qualifiedByName = "transliterate")
    @Mapping(target = "senderLastName", source = "sender.lastName", qualifiedByName = "transliterate")
    @Mapping(target = "senderMiddleName", source = "sender.middleName", qualifiedByName = "transliterate")
    @Mapping(target = "senderBirthDate", source = "sender.birthDate")
    @Mapping(target = "senderCountry", source = "sender.countryOfResidence")
    @Mapping(target = "senderFullAddress", ignore = true)
    @Mapping(target = "senderState", ignore = true)
    @Mapping(target = "senderCity", ignore = true)
    @Mapping(target = "senderStreet", ignore = true)
    @Mapping(target = "senderZip", ignore = true)
    @Mapping(target = "coinpayTransferId", ignore = true)
    @Mapping(target = "statusMessage", ignore = true)
    @Mapping(target = "internalMessage", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Transfers toEntity(MtsCreateTransactionRequestDto request);

    @Named("transliterate")
    default String transliterate(String value) {
        return TransliterationUtil.transliterate(value);
    }

    /**
     * Разбирает полный адрес отправителя ({@code registrationAddress.full}) на компоненты
     * и раскладывает их по колонкам с транслитерацией.
     */
    @AfterMapping
    default void fillSenderAddress(MtsCreateTransactionRequestDto request, @MappingTarget Transfers.TransfersBuilder builder) {
        String full = extractFullAddress(request);
        if (full == null) {
            return;
        }
        ParsedAddress address = AddressParser.parse(full);
        builder.senderFullAddress(TransliterationUtil.transliterate(full));
        builder.senderState(TransliterationUtil.transliterate(address.state()));
        builder.senderCity(TransliterationUtil.transliterate(address.city()));
        builder.senderStreet(TransliterationUtil.transliterate(address.street()));
        builder.senderZip(truncate(TransliterationUtil.transliterate(address.zip())));
    }

    private String truncate(String value) {
        if (value == null) {
            return null;
        }
        return value.length() > TransfersMapper.MAX_ZIP_LENGTH ? value.substring(0, TransfersMapper.MAX_ZIP_LENGTH) : value;
    }

    private String extractFullAddress(MtsCreateTransactionRequestDto request) {
        if (request.sender() == null || request.sender().addresses() == null
                || request.sender().addresses().registrationAddress() == null) {
            return null;
        }
        return request.sender().addresses().registrationAddress().full();
    }

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
