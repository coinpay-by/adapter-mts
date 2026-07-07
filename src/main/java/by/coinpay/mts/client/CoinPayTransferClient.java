package by.coinpay.mts.client;

import by.coinpay.mts.models.dto.coinpay.transfer.request.CoinPayExecuteRequestDto;
import by.coinpay.mts.models.dto.coinpay.transfer.response.CoinPayExecuteResponseDto;
import by.coinpay.mts.models.dto.coinpay.transfer.response.TransferStatusResponseDto;
import by.coinpay.mts.config.CoinPayTransferClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "coinpay-transfer-api",
        url = "${mts-adapter.rest.coinpay-transfer.url}",
        configuration = CoinPayTransferClientConfig.class)
public interface CoinPayTransferClient {

    @PostMapping(value = "/api/v1/transfers", consumes = MediaType.APPLICATION_JSON_VALUE)
    CoinPayExecuteResponseDto createTransfer(@RequestBody CoinPayExecuteRequestDto request);

    @GetMapping("/api/v1/transfers/{id}/status")
    TransferStatusResponseDto getStatus(@PathVariable("id") UUID id);
}
