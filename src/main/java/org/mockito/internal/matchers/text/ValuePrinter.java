package org.mockito.internal.matchers.text;

import java.util.Iterator;

import static java.lang.String.valueOf;

/**
 * Prints a Java object value in a way humans can read it neatly.
 * Inspired on hamcrest. Used for printing arguments in verification errors.
 */
public class ValuePrinter {

    /**
     * Prints given value so that it is neatly readable by humans.
     * Handles explosive toString() implementations.
     */
    public static String print(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + value + "\"";
        } else if (value instanceof Character) {
            return printChar((Character) value);
        } else if (value.getClass().isArray()) {
            return printValues("[", ", ", "]", new org.mockito.internal.matchers.text.ArrayIterator(value));
        } else if (value instanceof FormattedText) {
            return (((FormattedText) value).getText());
        }

        return descriptionOf(value);
    }

    /**
     * Print values in a nice format, e.g. (1, 2, 3)
     *
     * @param start the beginning of the values, e.g. "("
     * @param separator the separator of values, e.g. ", "
     * @param end the end of the values, e.g. ")"
     * @param values the values to print
     *
     * @return neatly formatted value list
     */
    public static String printValues(String start, String separator, String end, Iterator values) {
        if(start == null){
            start = "(";
        }
        if (separator == null){
            separator = ",";
        }
        if (end == null){
            end = ")";
        }
        if (values == null){
            values = new ArrayIterator(new String[]{""});
        }

        StringBuilder sb = new StringBuilder(start);
        while(values.hasNext()) {
            sb.append(print(values.next()));
            if (values.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.append(end).toString();
    }

    private static String printChar(char value) {
        StringBuilder sb = new StringBuilder();
        sb.append('\'');
        switch (value) {
            case '"':
                sb.append("\\\"");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            default:
                sb.append(value);
        }
        sb.append('\'');
        return sb.toString();
    }

    private static String descriptionOf(Object value) {
        try {
            return valueOf(value);
        }
        catch (Exception e) {
            return value.getClass().getName() + "@" + Integer.toHexString(value.hashCode());
        }
    }
}
