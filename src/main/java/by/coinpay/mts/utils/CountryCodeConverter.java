package by.coinpay.mts.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Конвертация кодов стран ISO 3166-1 alpha-3 -> alpha-2 */
@UtilityClass
public class CountryCodeConverter {

    private final Map<String, String> ALPHA3_TO_ALPHA2 = buildMap();

    /** alpha-3 -> alpha-2. Возвращает null, если код неизвестен или входной null. */
    public String toAlpha2(String alpha3) {
        if (alpha3 == null) {
            return null;
        }
        return ALPHA3_TO_ALPHA2.get(alpha3.toUpperCase());
    }

    private Map<String, String> buildMap() {
        Map<String, String> map = new HashMap<>();
        for (String alpha2 : Locale.getISOCountries()) {
            String alpha3 = Locale.of("", alpha2).getISO3Country();
            if (!alpha3.isEmpty()) {
                map.put(alpha3, alpha2);
            }
        }
        return map;
    }
}
