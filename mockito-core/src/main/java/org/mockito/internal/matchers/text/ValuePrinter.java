/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import static java.util.Comparator.comparing;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;

/**
 * Prints a Java object value in a way humans can read it neatly.
 * Inspired on hamcrest. Used for printing arguments in verification errors.
 */
public class ValuePrinter {

    private ValuePrinter() {}

    /**
     * Prints given value so that it is neatly readable by humans.
     * Handles explosive toString() implementations.
     */
    public static String print(final Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return '"' + value.toString() + '"';
        }
        if (value instanceof Character) {
            return printChar((Character) value);
        }
        if (value instanceof Long) {
            return value + "L";
        }
        if (value instanceof Double) {
            return value + "d";
        }
        if (value instanceof Float) {
            return value + "f";
        }
        if (value instanceof Short) {
            return "(short) " + value;
        }
        if (value instanceof Byte) {
            return String.format("(byte) 0x%02X", (Byte) value);
        }
        if (value instanceof Map) {
            return printMap((Map<?, ?>) value);
        }
        if (value.getClass().isArray()) {
            return printValues(
                    "[",
                    ", ",
                    "]",
                    new Iterator<Object>() {
                        private int currentIndex = 0;

                        @Override
                        public boolean hasNext() {
                            return currentIndex < Array.getLength(value);
                        }

                        public Object next() {
                            return Array.get(value, currentIndex++);
                        }

                        public void remove() {
                            throw new UnsupportedOperationException(
                                    "cannot remove items from an array");
                        }
                    });
        }
        if (value instanceof FormattedText) {
            return (((FormattedText) value).getText());
        }

        return descriptionOf(value);
    }

    private static String printMap(Map<?, ?> map) {
        StringBuilder result = new StringBuilder();
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = iterator.next();
            result.append(print(entry.getKey())).append(" = ").append(print(entry.getValue()));
            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        return "{" + result + "}";
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
    public static String printValues(
            String start, String separator, String end, Iterator<?> values) {
        if (start == null) {
            start = "(";
        }
        if (separator == null) {
            separator = ",";
        }
        if (end == null) {
            end = ")";
        }

        StringBuilder sb = new StringBuilder(start);
        while (values.hasNext()) {
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

    private static final int MAX_FIELD_VALUE_LENGTH = 100;

    /**
     * Return string representation of given object.
     * If 'value' has its own {@code toString()} method, it's called.
     * If 'value' does not declare its own {@code toString()} then {@code Object.toString()} is useless for a
     * human-readable diff, so fields are printed reflectively instead.
     *
     * @param value any java object
     * @return String representation of this object (non-nullable)
     */
    private static String descriptionOf(Object value) {
        try {
            if (!declaresOwnToString(value.getClass())) {
                return reflectiveToString(value);
            }
            return String.valueOf(value);
        } catch (RuntimeException e) {
            return value.getClass().getName() + "@" + Integer.toHexString(value.hashCode());
        }
    }

    private static boolean declaresOwnToString(Class<?> type) {
        try {
            return type.getMethod("toString").getDeclaringClass() != Object.class;
        } catch (NoSuchMethodException neverHappensBecauseObjectDeclaresToString) {
            return false;
        }
    }

    private static String reflectiveToString(Object value) {
        MemberAccessor accessor = Plugins.getMemberAccessor();
        StringBuilder result = new StringBuilder(value.getClass().getSimpleName()).append('[');
        boolean first = true;
        for (Class<?> type = value.getClass();
                type != null && type != Object.class;
                type = type.getSuperclass()) {
            for (Field field : fields(type)) {
                if (!first) {
                    result.append(", ");
                }
                first = false;
                result.append(field.getName())
                        .append('=')
                        .append(fieldValue(accessor, field, value));
            }
        }
        return result.append(']').toString();
    }

    /**
     * Sorts fields by name - this guarantees the same output every time, thus avoiding flaky tests.
     */
    private static Iterable<Field> fields(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> !field.isSynthetic())
                .sorted(comparing(Field::getName))
                .collect(Collectors.toList());
    }

    private static String fieldValue(MemberAccessor accessor, Field field, Object target) {
        try {
            return truncate(String.valueOf(accessor.get(field, target)));
        } catch (RuntimeException | IllegalAccessException fieldNotReadable) {
            return "<unavailable>";
        }
    }

    private static String truncate(String text) {
        return text.length() > MAX_FIELD_VALUE_LENGTH
                ? text.substring(0, MAX_FIELD_VALUE_LENGTH) + "..."
                : text;
    }
}
