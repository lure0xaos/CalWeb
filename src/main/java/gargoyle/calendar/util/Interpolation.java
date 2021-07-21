package gargoyle.calendar.util;

import java.text.MessageFormat;
import java.util.Map;
import java.util.function.Function;

public final class Interpolation {
    private Interpolation() {
    }

    public static String interpolate(String text, Map<String, Object> params) {
        String ret = text != null ? text : "";
        if (text != null && !text.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                ret = ret.replace(MessageFormat.format("'{'{0}'}'", entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        return ret;
    }

    public static String cutTail(String text, String tail) {
        return text.toLowerCase().endsWith(tail.toLowerCase()) ? text.substring(0, text.length() - tail.length()) : text;
    }

    public static String findFirstNotEmpty(String... texts) {
        for (String text : texts) {
            if (text != null && !text.isEmpty()) return text;
        }
        return "";
    }

    public static <T> String findFirstNotEmpty(Iterable<T> iterable, Function<T, String> formatter) {
        for (T item : iterable) {
            if (item != null) {
                String text = formatter.apply(item);
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }
        return "";
    }

    public static String splitWords(String text, SPLIT_WORDS_OPTIONS options) {
        StringBuilder result = new StringBuilder();
        boolean word;
        for (char c : text.toCharArray()) {
            boolean begin = result.length() == 0;
            if (!Character.isLetterOrDigit(c)) {
                if (!begin) result.append(' ');
                continue;
            } else {
                if (Character.isUpperCase(c)) {
                    word = true;
                    if (!begin) result.append(' ');
                } else {
                    word = false;
                }
            }
            switch (options) {
                case FIRST_UPPER_EVERY:
                    if (word) {
                        result.append(Character.toUpperCase(c));
                    } else {
                        result.append(Character.toLowerCase(c));
                    }
                    break;
                case FIRST_UPPER:
                    if (word && begin) {
                        result.append(Character.toUpperCase(c));
                    } else {
                        result.append(Character.toLowerCase(c));
                    }
                    break;
                case ALL_LOWER:
                    result.append(Character.toLowerCase(c));
                    break;
                case ALL_UPPER:
                    result.append(Character.toUpperCase(c));
                    break;
            }
        }
        return result.toString();
    }

    public enum SPLIT_WORDS_OPTIONS {
        ALL_UPPER,
        ALL_LOWER,
        FIRST_UPPER,
        FIRST_UPPER_EVERY,
    }
}
