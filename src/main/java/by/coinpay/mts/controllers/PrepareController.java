package by.coinpay.mts.controllers;

import by.coinpay.mts.models.dto.mts.transfer.MtsCreateTransactionRequestDto;
import by.coinpay.mts.models.dto.mts.transfer.MtsCreateTransactionResponseDto;
import by.coinpay.mts.service.PrepareService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrepareController {

    PrepareService prepareService;

    @PostMapping("/create")
    public MtsCreateTransactionResponseDto create(@Valid @RequestBody MtsCreateTransactionRequestDto request) {
        return prepareService.create(request);
    }
}
