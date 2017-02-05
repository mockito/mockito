package org.mockito.internal.util;

import static java.util.Arrays.asList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern CAPS = Pattern.compile("([A-Z\\d][^A-Z\\d]*)");

    private StringUtil() {}
    /**
     * @param text
     *            to have the first line removed
     * @return less first line
     */
    public static String removeFirstLine(String text) {
        return text.replaceFirst(".*?\n", "");
    }

    /**
     * Joins Strings with line break character. It adds line break in front, too.
     * This makes it something like 'format' no really 'join'.
     */
    public static String join(Object ... linesToBreak) {
        return join("\n", asList(linesToBreak));
    }

    /**
     * Joins Strings with EOL character
     */
    public static String join(String start, Iterable<?> lines) {
        StringBuilder out = new StringBuilder(start);
        for (Object line : lines) {
            out.append(line.toString()).append("\n");
        }
        int lastBreak = out.lastIndexOf("\n");
        return out.replace(lastBreak, lastBreak+1, "").toString();
    }

    public static String decamelizeMatcher(String className) {
        if (className.length() == 0) {
            return "<custom argument matcher>";
        }

        String decamelized = decamelizeClassName(className);

        if (decamelized.length() == 0) {
            return "<" + className + ">";
        }

        return "<" + decamelized + ">";
    }

    private static String decamelizeClassName(String className) {
        Matcher match = CAPS.matcher(className);
        StringBuilder deCameled = new StringBuilder();
        while (match.find()) {
            if (deCameled.length() == 0) {
                deCameled.append(match.group());
            } else {
                deCameled.append(" ");
                deCameled.append(match.group().toLowerCase());
            }
        }
        return deCameled.toString();
    }
}
