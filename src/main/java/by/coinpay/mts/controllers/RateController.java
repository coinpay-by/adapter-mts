package by.coinpay.mts.controllers;

import by.coinpay.mts.models.dto.mts.rates.request.MtsRateRequestDto;
import by.coinpay.mts.models.dto.mts.rates.response.MtsRateResponseDto;
import by.coinpay.mts.service.RateService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateController {

    RateService rateService;

    @GetMapping("/rate")
    public MtsRateResponseDto getRate(@Valid @ModelAttribute MtsRateRequestDto request) {
        return rateService.getRate(request);
    }
}
