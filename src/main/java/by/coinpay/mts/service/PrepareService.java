package by.coinpay.mts.service;

import by.coinpay.mts.exceptions.TransferAlreadyExistsException;
import by.coinpay.mts.mapper.TransfersMapper;
import by.coinpay.mts.models.dto.mts.transfer.MtsCreateTransactionRequestDto;
import by.coinpay.mts.models.dto.mts.transfer.MtsCreateTransactionResponseDto;
import by.coinpay.mts.models.entity.Transfers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrepareService {

    TransfersService transfersService;

    TransfersMapper transfersMapper;

    @Transactional
    public MtsCreateTransactionResponseDto create(MtsCreateTransactionRequestDto request) {
        if (transfersService.existsByTransactionId(request.transactionId())) {
            log.warn("Transfer with transactionId: {} already exists", request.transactionId());
            throw new TransferAlreadyExistsException(request.transactionId().toString());
        }

        Transfers entity = createTransfer(request);
        return transfersMapper.toResponse(entity);
    }

    private Transfers createTransfer(MtsCreateTransactionRequestDto request) {
        Transfers entity = transfersMapper.toEntity(request);
        return transfersService.save(entity);
    }
}
