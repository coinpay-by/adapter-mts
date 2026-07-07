package by.coinpay.mts.models.dto.coinpay.transfer.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CoinPayExecuteRequestDto(
        String externalId,
        UUID terminalId,
        Money source,
        Money target,
        String rate,
        String executionDeadline,
        String description,
        Sender sender,
        Recipient recipient
) {

    public record Money(
            String currency,
            String amount
    ) {
    }

    public record Sender(
            String firstName,
            String lastName,
            String middleName,
            String birthDate,
            String fullAddress,
            String country,
            String state,
            String city,
            String street,
            String zip
    ) {
    }

    public record Recipient(
            String card,
            String holder
    ) {
    }
}
