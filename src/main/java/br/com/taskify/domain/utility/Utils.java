package br.com.taskify.domain.utility;

import java.text.Normalizer;

public class Utils {
    private Utils() {}

    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    public static String formatToLike(String query) {
        return "%".concat(unaccent(query.trim()).toLowerCase()).concat("%");
    }
}
