package by.coinpay.mts.controllers;

import by.coinpay.mts.models.dto.mts.transfer.MtsCreateTransactionResponseDto;
import by.coinpay.mts.service.ConfirmService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfirmController {

    ConfirmService confirmService;

    @PostMapping("/{transactionId}/confirm")
    public MtsCreateTransactionResponseDto confirm(@PathVariable UUID transactionId) {
        return confirmService.confirm(transactionId);
    }
}
