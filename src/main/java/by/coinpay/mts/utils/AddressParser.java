package by.coinpay.mts.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Разбирает свободную строку адреса (поле {@code registrationAddress.full}) на компоненты:
 * регион (state), город (city), улицу (street) и почтовый индекс (zip).
 *
 * <p>Строка ожидается как перечисление через запятую, напр.
 * {@code "Красноярский край, Октябрьский район, Красноярск, Ул. Зеленая, д. 7, 660001"}.
 * Классификация — по ключевым словам (край/область, район, ул./проспект и т.п.), индекс — по
 * шаблону из 6 цифр. Разбор эвристический: мусорные токены (только знаки/цифры) отбрасываются,
 * порядок компонентов не гарантируется на 100%, поэтому city заполняется по остаточному принципу.
 */
@UtilityClass
public class AddressParser {

    private static final Pattern ZIP = Pattern.compile("\\b\\d{6}\\b");
    private static final Pattern HAS_LETTER = Pattern.compile("\\p{L}");
    private static final Pattern HOUSE = Pattern.compile("^(д\\.|дом|д\\s|№|\\d)");

    private static final Set<String> REGION_KW = Set.of("край", "область", "обл", "республика", "респ", "округ", "ао");
    private static final Set<String> DISTRICT_KW = Set.of("район", "р-н");
    private static final Set<String> STREET_KW = Set.of(
            "ул", "улица", "проспект", "пр-кт", "пр-т", "пр", "переулок", "пер",
            "бульвар", "б-р", "набережная", "наб", "шоссе", "ш", "проезд", "тупик",
            "аллея", "площадь", "пл", "линия", "мкр", "микрорайон");

    /** Разобранный адрес; любое поле может быть null, если его не удалось определить. */
    public record ParsedAddress(String state, String city, String street, String zip) {
    }

    public ParsedAddress parse(String full) {
        if (full == null || full.isBlank()) {
            return new ParsedAddress(null, null, null, null);
        }

        String zip = firstMatch(full);
        List<String> tokens = meaningfulTokens(full, zip);

        String state = null;
        String street = null;
        Integer streetIdx = null;
        for (int i = 0; i < tokens.size(); i++) {
            String low = normalize(tokens.get(i));
            if (state == null && containsKeyword(low, REGION_KW)) {
                state = tokens.get(i);
            } else if (street == null && containsKeyword(low, STREET_KW)) {
                street = tokens.get(i);
                streetIdx = i;
            }
        }

        // К улице присоединяем следующий токен, если это номер дома/корпуса.
        if (streetIdx != null && streetIdx + 1 < tokens.size()
                && HOUSE.matcher(tokens.get(streetIdx + 1)).find()) {
            street = street + ", " + tokens.get(streetIdx + 1);
        }

        String city = firstCity(tokens, state, streetIdx);

        // Индекс не нашли — в колонку zip кладём город.
        if (zip == null) {
            zip = city;
        }

        return new ParsedAddress(state, city, street, zip);
    }

    /** Первый токен без региона/района/улицы — считаем его городом. */
    private String firstCity(List<String> tokens, String state, Integer streetIdx) {
        for (int i = 0; i < tokens.size(); i++) {
            if (streetIdx != null && i >= streetIdx) {
                break;
            }
            String token = tokens.get(i);
            String low = normalize(token);
            if (token.equals(state) || containsKeyword(low, REGION_KW)
                    || containsKeyword(low, DISTRICT_KW) || containsKeyword(low, STREET_KW)) {
                continue;
            }
            return token;
        }
        return null;
    }

    private List<String> meaningfulTokens(String full, String zip) {
        List<String> tokens = new ArrayList<>();
        for (String part : full.split(",")) {
            String token = part.trim();
            if (zip != null) {
                token = token.replace(zip, "").trim();
            }
            if (HAS_LETTER.matcher(token).find()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private boolean containsKeyword(String normalized, Set<String> keywords) {
        for (String word : normalized.split("\\s+")) {
            if (keywords.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /** Нижний регистр без точек — для сравнения с ключевыми словами ("Ул." -> "ул"). */
    private String normalize(String token) {
        return token.toLowerCase().replace(".", "");
    }

    private String firstMatch(String text) {
        var matcher = ZIP.matcher(text);
        return matcher.find() ? matcher.group() : null;
    }
}
