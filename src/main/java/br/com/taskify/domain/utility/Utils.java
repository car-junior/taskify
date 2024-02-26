package br.com.taskify.domain.utility;

import java.text.Normalizer;
import java.util.Optional;

public class Utils {
    private Utils() {}

    public static final String UNACCENT = "unaccent";
    public static boolean isPresent(Object value) {
        return Optional.ofNullable(value).isPresent();
    }
    public static boolean isNotEmpty(String value) {
        return isPresent(value) && value.trim().isEmpty();
    }

    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    public static String formatToLike(String query) {
        return "%".concat(unaccent(query.trim()).toLowerCase()).concat("%");
    }
}
