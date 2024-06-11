package javatechy.codegen.common;

import java.util.Map;
import java.util.Map.Entry;

public class Common {

    public static String replaceParams(String text, String key, String value) {
        if (value != null)
            text = text.replace(Constant.TEMPLATE_START_CHARACTER + key + Constant.TEMPLATE_END_CHARACTER, value);
        return text;
    }

    public static String replaceParams(String text, Map<String, String> valuesMap) {
        for (Entry<String, String> entry : valuesMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            text = Common.replaceParams(text, key, value);
        }
        return text;
    }

    public static String toCamelCase(final String init) {
        if (init == null)
            return null;
        final StringBuilder ret = new StringBuilder(init.length());
        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1)
                    .toUpperCase());
                ret.append(word.substring(1)
                    .toLowerCase());
            }
            if (!(ret.length() == init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }
}
