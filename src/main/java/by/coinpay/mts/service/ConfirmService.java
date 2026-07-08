package by.coinpay.mts.service;

import by.coinpay.mts.enums.TransactionStatus;
import by.coinpay.mts.exceptions.EntityNotFoundException;
import by.coinpay.mts.mapper.TransfersMapper;
import by.coinpay.mts.models.dto.mts.transfer.MtsCreateTransactionResponseDto;
import by.coinpay.mts.models.entity.Transfers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfirmService {

    TransfersService transfersService;
    TransfersMapper transfersMapper;

    @Transactional
    public MtsCreateTransactionResponseDto confirm(UUID transactionId) {
        Transfers transfer = transfersService
                .findByTransactionId(transactionId)
                .orElseThrow(() -> {
                    log.warn("Operation with transactionId: {} not found", transactionId);
                    return new EntityNotFoundException(transactionId.toString());
                });

        if (transfer.getTransactionStatus() == TransactionStatus.CREATED) {
            transfer.setTransactionStatus(TransactionStatus.PROCESSING);
            transfersService.update(transfer);
        }

        return transfersMapper.toResponse(transfer);
    }
}
