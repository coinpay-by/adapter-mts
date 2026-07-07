package by.coinpay.mts.models.dto.mts.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressDto(
        String countryCode,
        String zipCode,
        String region,
        String district,
        String locality,
        String city,
        String street,
        String house,
        String block,
        String building,
        String apartment,
        String full
) {
}
