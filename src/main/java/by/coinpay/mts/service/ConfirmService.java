package by.coinpay.mts.service;

import by.coinpay.mts.enums.TransactionStatus;
import by.coinpay.mts.mapper.TransfersMapper;
import by.coinpay.mts.models.dto.mts.transfer.response.MtsCreateTransactionResponseDto;
import by.coinpay.mts.models.entity.Transfers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfirmService {

    TransfersService transfersService;
    TransfersMapper transfersMapper;

    @Transactional
    public MtsCreateTransactionResponseDto confirm(UUID transactionId) {
        Transfers transfer = transfersService.getByTransactionId(transactionId);

        if (transfer.getTransactionStatus() == TransactionStatus.CREATED) {
            transfer.setTransactionStatus(TransactionStatus.PROCESSING);
            transfersService.update(transfer);
        }

        return transfersMapper.toResponse(transfer);
    }
}
