package by.coinpay.mts.service;

import by.coinpay.mts.mapper.TransfersMapper;
import by.coinpay.mts.models.dto.mts.ErrorDto;
import by.coinpay.mts.models.dto.mts.registry.MtsRegistryResponseDto;
import by.coinpay.mts.models.entity.Transfers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistryService {

    TransfersService transfersService;
    TransfersMapper transfersMapper;

    public MtsRegistryResponseDto getRegistry(LocalDate date) {
        List<Transfers> transfers = transfersService.findByDate(date);

        List<MtsRegistryResponseDto.Transaction> items = transfers.stream()
                .map(transfersMapper::toRegistryTransaction)
                .toList();

        return new MtsRegistryResponseDto(items, ErrorDto.success());
    }
}
