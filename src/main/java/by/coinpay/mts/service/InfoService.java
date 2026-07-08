package by.coinpay.mts.service;

import by.coinpay.mts.exceptions.EntityNotFoundException;
import by.coinpay.mts.mapper.TransfersMapper;
import by.coinpay.mts.models.dto.mts.status.MtsStatusResponseDto;
import by.coinpay.mts.models.entity.Transfers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InfoService {

    TransfersService transfersService;

    TransfersMapper transfersMapper;

    public MtsStatusResponseDto getStatus(UUID transactionId) {
        Transfers transfer = transfersService
                .findByTransactionId(transactionId)
                .orElseThrow(() -> {
                    log.warn("Operation with transactionId: {} not found", transactionId);
                    return new EntityNotFoundException(transactionId.toString());
                });

        return transfersMapper.toStatusResponse(transfer);
    }
}
