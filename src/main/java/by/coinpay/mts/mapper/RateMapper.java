package by.coinpay.mts.mapper;

import by.coinpay.mts.config.CommonMapperConfig;
import by.coinpay.mts.models.dto.mts.ErrorDto;
import by.coinpay.mts.models.dto.mts.rates.request.MtsRateRequestDto;
import by.coinpay.mts.models.dto.coinpay.rates.response.CoinPayRatesResponse;
import by.coinpay.mts.models.dto.mts.rates.response.MtsRateResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(config = CommonMapperConfig.class, imports = ErrorDto.class)
public interface RateMapper {

    @Mapping(target = "serviceId", source = "request.serviceId")
    @Mapping(target = "currencyFrom", source = "request.currencyFrom")
    @Mapping(target = "currencyTo", source = "request.currencyTo")
    @Mapping(target = "amountFrom", source = "rates.amount")
    @Mapping(target = "amountTo", source = "rates.convertedAmount")
    @Mapping(target = "exchangeRate", source = "rates.convertedRate")
    @Mapping(target = "multy", source = "request.multy")
    @Mapping(target = "sellRate", source = "rates.convertedRate")
    @Mapping(target = "buyRate", source = "rates.convertedRate")
    @Mapping(target = "error", expression = "java(ErrorDto.success())")
    MtsRateResponseDto toResponse(MtsRateRequestDto request, CoinPayRatesResponse rates);

    @Mapping(target = "serviceId", source = "request.serviceId")
    @Mapping(target = "currencyFrom", source = "request.currencyFrom")
    @Mapping(target = "currencyTo", source = "request.currencyTo")
    @Mapping(target = "amountFrom", ignore = true)
    @Mapping(target = "amountTo", ignore = true)
    @Mapping(target = "exchangeRate", source = "rate")
    @Mapping(target = "multy", source = "request.multy")
    @Mapping(target = "sellRate", source = "rate")
    @Mapping(target = "buyRate", source = "rate")
    @Mapping(target = "error", expression = "java(ErrorDto.success())")
    MtsRateResponseDto toResponse(MtsRateRequestDto request, BigDecimal rate);
}
