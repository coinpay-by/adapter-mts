package by.coinpay.mts.controllers;

import by.coinpay.mts.models.dto.mts.status.MtsStatusResponseDto;
import by.coinpay.mts.service.InfoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InfoController {

    InfoService infoService;

    @GetMapping("/{transactionId}/status")
    public MtsStatusResponseDto getStatus(@PathVariable UUID transactionId) {
        return infoService.getStatus(transactionId);
    }
}
