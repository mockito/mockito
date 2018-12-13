/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.util.*;

/**
 * This class provides markers for argument-matchers; such a marker is the value passed to a mock method when a matcher
 * is used on this mock, whether in stubbing or in verification.
 */
public class MatcherMarkers {

    private static final Object GENERIC_MARKER = null;

    private static final Boolean MARKER_BOOLEAN = false;
    private static final Character MARKER_CHAR = '\u0000';
    private static final Byte MARKER_BYTE = 0;
    private static final Short MARKER_SHORT = 1398;
    private static final Integer MARKER_INT = 13981398;
    private static final Long MARKER_LONG = 139813981398L;
    private static final Float MARKER_FLOAT = 13981398F;
    private static final Double MARKER_DOUBLE = 139813981398D;
    private static final String MARKER_STRING = "138913891389";

    private static final List MARKER_LIST = Collections.unmodifiableList(new ArrayList(0));
    private static final Set MARKER_SET = Collections.unmodifiableSet(new HashSet(0));
    private static final Map MARKER_MAP = Collections.unmodifiableMap(new HashMap(0));

    /**
     * @param clazz The given type's class.
     * @param <T>   The given type.
     * @return The marker of argument-matchers that their argument is of the given type.
     */
    public static <T> T markerOf(Class<T> clazz) {

        if (clazz == boolean.class || clazz == Boolean.class) return (T) MARKER_BOOLEAN;

        if (clazz == char.class || clazz == Character.class) return (T) MARKER_CHAR;

        if (clazz == byte.class || clazz == Byte.class) return (T) MARKER_BYTE;

        if (clazz == short.class || clazz == Short.class) return (T) MARKER_SHORT;

        if (clazz == int.class || clazz == Integer.class) return (T) MARKER_INT;

        if (clazz == long.class || clazz == Long.class) return (T) MARKER_LONG;

        if (clazz == float.class || clazz == Float.class) return (T) MARKER_FLOAT;

        if (clazz == double.class || clazz == Double.class) return (T) MARKER_DOUBLE;

        if (clazz == String.class) return (T) MARKER_STRING;

        if (clazz == List.class) return (T) MARKER_LIST;

        if (clazz == Set.class) return (T) MARKER_SET;

        if (clazz == Map.class) return (T) MARKER_MAP;

        if (clazz == Collection.class) return (T) MARKER_LIST;

        if (clazz == Iterable.class) return (T) MARKER_LIST;

        return (T) GENERIC_MARKER;
    }

    /**
     * @param value The given value.
     * @return The marker of argument-matchers that their argument is of the same type as the given value.
     */
    public static Object markerOf(Object value) {

        if (value == null) return GENERIC_MARKER;

        return markerOf(value.getClass());
    }

    /**
     * @return A generic marker; i.e, one that serves many different types of argument-matchers.
     */
    public static Object genericMarker() {

        return GENERIC_MARKER;
    }

    /**
     * @param value The given value.
     * @return <code>true</code> if the given value is a matcher-marker, <code>false</code> otherwise.
     */
    public static boolean isMarker(Object value) {

        if (value == null) return true;

        if (GENERIC_MARKER == value || MARKER_LIST == value || MARKER_SET == value || MARKER_MAP == value) return true;

        return value.equals(markerOf(value.getClass()));
    }

}
