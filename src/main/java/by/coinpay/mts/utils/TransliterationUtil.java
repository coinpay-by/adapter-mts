package by.coinpay.mts.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Транслитерация кириллицы в латиницу (ГОСТ-подобная схема).
 *
 * <p>Таблица соответствий строится один раз при загрузке класса, поиск замены по символу — O(1)
 * через {@link Map}. Символы вне таблицы (латиница, цифры, пробелы, дефисы) остаются как есть.
 */
@UtilityClass
public class TransliterationUtil {

    private final char[] CYRILLIC = {
            'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М',
            'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ',
            'Ы', 'Ь', 'Э', 'Ю', 'Я',
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м',
            'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ',
            'ы', 'ь', 'э', 'ю', 'я'
    };

    private final String[] LATIN = {
            "A", "B", "V", "G", "D", "E", "YO", "ZH", "Z", "I", "Y", "K", "L", "M",
            "N", "O", "P", "R", "S", "T", "U", "F", "KH", "TS", "CH", "SH", "SHCH", "",
            "Y", "", "E", "YU", "YA",
            "a", "b", "v", "g", "d", "e", "yo", "zh", "z", "i", "y", "k", "l", "m",
            "n", "o", "p", "r", "s", "t", "u", "f", "kh", "ts", "ch", "sh", "shch", "",
            "y", "", "e", "yu", "ya"
    };

    private final Map<Character, String> TABLE = buildTable();

    /** Транслитерирует строку. null -> null; символы вне таблицы остаются без изменений. */
    public String transliterate(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            String replacement = TABLE.get(c);
            sb.append(replacement != null ? replacement : c);
        }
        return sb.toString();
    }

    private Map<Character, String> buildTable() {
        Map<Character, String> table = new HashMap<>(CYRILLIC.length * 2);
        for (int i = 0; i < CYRILLIC.length; i++) {
            table.put(CYRILLIC[i], LATIN[i]);
        }
        return table;
    }
}
