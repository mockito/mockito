/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.mockito.creation.instance.InstantiationException;

/**
 * Helper class to work with features that were introduced in Java versions after 1.5.
 * This class uses reflection in most places to avoid coupling with a newer JDK.
 */
public final class JavaEightUtil {

    // No need for volatile, these optionals are already safe singletons.
    private static Object emptyOptional;
    private static Object emptyOptionalDouble;
    private static Object emptyOptionalInt;
    private static Object emptyOptionalLong;
    private static Object emptyDuration;
    private static Object emptyPeriod;
    private static Object emptyInstant;
    private static Object emptyLocalDate;
    private static Object emptyLocalDateTime;
    private static Object emptyLocalTime;
    private static Object emptyOffsetDateTime;
    private static Object emptyOffsetTime;
    private static Object emptyZonedDateTime;

    private JavaEightUtil() {
        // utility class
    }

    /**
     * Creates an empty Optional using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty Optional.
     */
    public static Object emptyOptional() {
        // no need for double-checked locking
        if (emptyOptional != null) {
            return emptyOptional;
        }

        return emptyOptional = invokeNullaryFactoryMethod("java.util.Optional", "empty");
    }


    /**
     * Creates an empty OptionalDouble using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty OptionalDouble.
     */
    public static Object emptyOptionalDouble() {
        // no need for double-checked locking
        if (emptyOptionalDouble != null) {
            return emptyOptionalDouble;
        }

        return emptyOptionalDouble = invokeNullaryFactoryMethod("java.util.OptionalDouble", "empty");
    }

    /**
     * Creates an empty OptionalInt using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty OptionalInt.
     */
    public static Object emptyOptionalInt() {
        // no need for double-checked locking
        if (emptyOptionalInt != null) {
            return emptyOptionalInt;
        }

        return emptyOptionalInt = invokeNullaryFactoryMethod("java.util.OptionalInt", "empty");
    }

    /**
     * Creates an empty OptionalLong using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty OptionalLong.
     */
    public static Object emptyOptionalLong() {
        // no need for double-checked locking
        if (emptyOptionalLong != null) {
            return emptyOptionalLong;
        }

        return emptyOptionalLong = invokeNullaryFactoryMethod("java.util.OptionalLong", "empty");
    }

    /**
     * Creates an empty Stream using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty Stream.
     */
    public static Object emptyStream() {
        // note: the empty stream can not be stored as a singleton.
        return invokeNullaryFactoryMethod("java.util.stream.Stream", "empty");
    }

    /**
     * Creates an empty DoubleStream using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty DoubleStream.
     */
    public static Object emptyDoubleStream() {
        // note: the empty stream can not be stored as a singleton.
        return invokeNullaryFactoryMethod("java.util.stream.DoubleStream", "empty");
    }

    /**
     * Creates an empty IntStream using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty IntStream.
     */
    public static Object emptyIntStream() {
        // note: the empty stream can not be stored as a singleton.
        return invokeNullaryFactoryMethod("java.util.stream.IntStream", "empty");
    }

    /**
     * Creates an empty LongStream using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty LongStream.
     */
    public static Object emptyLongStream() {
        // note: the empty stream can not be stored as a singleton.
        return invokeNullaryFactoryMethod("java.util.stream.LongStream", "empty");
    }


    /**
     * Creates an empty Duration using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty (ZERO) Duration.
     */
    public static Object emptyDuration() {
        // no need for double-checked locking
        if (emptyDuration != null) {
            return emptyDuration;
        }

        return emptyDuration = getStaticFieldValue("java.time.Duration", "ZERO");
    }

    /**
     * Creates an empty Period using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty (ZERO) Period.
     */
    public static Object emptyPeriod() {
        // no need for double-checked locking
        if (emptyPeriod != null) {
            return emptyPeriod;
        }

        return emptyPeriod = getStaticFieldValue("java.time.Period", "ZERO");
    }

    /**
     * Creates an empty Instant using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty (EPOCH) Instant.
     */
    public static Object emptyInstant() {
        // no need for double-checked locking
        if (emptyInstant != null) {
            return emptyInstant;
        }

        return emptyInstant = getStaticFieldValue("java.time.Instant", "EPOCH");
    }

    /**
     * Creates an empty LocalDate using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty (EPOCH) LocalDate.
     */
    public static Object emptyLocalDate() {
        // no need for double-checked locking
        if (emptyLocalDate != null) {
            return emptyLocalDate;
        }

        return emptyLocalDate = getStaticFieldValue("java.time.LocalDate", "EPOCH");
    }

    /**
     * Creates an empty LocalDateTime using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty (EPOCH) LocalDateTime at midnight (UTC).
     */
    public static Object emptyLocalDateTime() {
        // no need for double-checked locking
        if (emptyLocalDateTime != null) {
            return emptyLocalDateTime;
        }

        final Method localDateTimeOfInstantMethod = getMethod("java.time.LocalDateTime", "ofInstant",
            getClass("java.time.Instant"), getClass("java.time.ZoneId"));
        final Method zoneIdOfMethod = getMethod("java.time.ZoneId", "of", String.class);
        return emptyLocalDateTime = invokeNullaryFactoryMethodWithArgs(localDateTimeOfInstantMethod,
            getStaticFieldValue("java.time.Instant", "EPOCH"),
            invokeNullaryFactoryMethodWithArgs(zoneIdOfMethod, "UTC"));
    }

    /**
     * Creates an empty LocalTime using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty (midnight) LocalTime.
     */
    public static Object emptyLocalTime() {
        // no need for double-checked locking
        if (emptyLocalTime != null) {
            return emptyLocalTime;
        }

        return emptyLocalTime = getStaticFieldValue("java.time.LocalTime","MIN");
    }

    /**
     * Creates an empty OffsetDateTime using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty (EPOCH) OffsetDateTime at midnight (UTC).
     */
    public static Object emptyOffsetDateTime() {
        // no need for double-checked locking
        if (emptyOffsetDateTime != null) {
            return emptyOffsetDateTime;
        }

        final Method offsetDateTimeOfInstantMethod = getMethod("java.time.OffsetDateTime", "ofInstant",
            getClass("java.time.Instant"), getClass("java.time.ZoneId"));
        final Method zoneIdOfMethod = getMethod("java.time.ZoneId", "of", String.class);
        return emptyOffsetDateTime = invokeNullaryFactoryMethodWithArgs(offsetDateTimeOfInstantMethod,
            getStaticFieldValue("java.time.Instant", "EPOCH"),
            invokeNullaryFactoryMethodWithArgs(zoneIdOfMethod, "UTC"));
    }

    /**
     * Creates an empty OffsetTime using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty OffsetTime at midnight (UTC).
     */
    public static Object emptyOffsetTime() {
        // no need for double-checked locking
        if (emptyOffsetTime != null) {
            return emptyOffsetTime;
        }

        final Method method = getMethod("java.time.OffsetTime", "of",
            getClass("java.time.LocalTime"), getClass("java.time.ZoneOffset"));
        return emptyOffsetTime = invokeNullaryFactoryMethodWithArgs(method,
            getStaticFieldValue("java.time.LocalTime", "MIN"),
            getStaticFieldValue("java.time.ZoneOffset", "UTC"));
    }

    /**
     * Creates an empty ZonedDateTime using reflection to stay backwards-compatible with older JDKs.
     *
     * @return an empty (EPOCH) ZonedDateTime at midnight (UTC).
     */
    public static Object emptyZonedDateTime() {
        // no need for double-checked locking
        if (emptyZonedDateTime != null) {
            return emptyZonedDateTime;
        }

        final Method zonedDateTimeOfInstantMethod = getMethod("java.time.ZonedDateTime", "ofInstant",
            getClass("java.time.Instant"), getClass("java.time.ZoneId"));
        final Method zoneIdOfMethod = getMethod("java.time.ZoneId", "of", String.class);
        return emptyZonedDateTime = invokeNullaryFactoryMethodWithArgs(zonedDateTimeOfInstantMethod,
            getStaticFieldValue("java.time.Instant", "EPOCH"),
            invokeNullaryFactoryMethodWithArgs(zoneIdOfMethod, "UTC"));
    }

    /**
     * Invokes a nullary static factory method using reflection to stay backwards-compatible with older JDKs.
     *
     * @param fqcn The fully qualified class name of the type to be produced.
     * @param methodName The name of the factory method.
     * @return the object produced.
     */
    private static Object invokeNullaryFactoryMethod(final String fqcn, final String methodName) {
        try {
            final Method method = getMethod(fqcn, methodName);
            return method.invoke(null);
            // any exception is really unexpected since the type name has
            // already been verified
        } catch (final Exception e) {
            throw new InstantiationException(
                String.format("Could not create %s#%s(): %s", fqcn, methodName, e), e);
        }
    }

    /**
     * Invokes a nullary static factory method with arguments using reflection to stay backwards-compatible with older JDKs.
     *
     * @param method The method to be invoked.
     * @param args The arguments used for the method call.
     * @return the object produced.
     */
    private static Object invokeNullaryFactoryMethodWithArgs(final Method method, final Object... args) {
        try {
            return method.invoke(null, args);
            // any exception is really unexpected since the type name has
            // already been verified
        } catch (final Exception e) {
            throw new InstantiationException(
                String.format("Could not invoke %s#%s(): %s", "nazwaKlasy", method.getName(), e), e);
        }
    }

    /**
     * Gets a value of the classes' field using reflection to stay backwards-compatible with older JDKs.
     *
     * @param fqcn The fully qualified class name of the type to be produced.
     * @param fieldName The name of th classes' field which value is going to be returned.
     * @return the restored value.
     */
    private static Object getStaticFieldValue(final String fqcn, final String fieldName) {
        try {
            final Class<?> type = getClass(fqcn);
            final Field field = type.getDeclaredField(fieldName);
            return field.get(null);
            // any exception is really unexpected since the type name has
            // already been verified
        } catch (Exception e) {
            throw new InstantiationException(
                String.format("Could not get %s#%s(): %s", fqcn, fieldName, e), e);
        }
    }

    /**
     * Returns the {@code Class} object associated with the class or interface with the given string name.
     *
     * @param fqcn The fully qualified class name of the type to be produced.
     * @return the Class object for the class with the specified name.
     */
    private static Class<?> getClass(String fqcn) {
        try {
            return Class.forName(fqcn);
            // any exception is really unexpected since the type name has
            // already been verified
        } catch (ClassNotFoundException e) {
            throw new InstantiationException(
                String.format("Could not find %s: %s", fqcn, e), e);
        }
    }

    /**
     * Returns a Method object that reflects the specified public member method of the class or interface represented by the fully qualified class name.
     *
     * @param fqcn The fully qualified class name of the type to be produced.
     * @param methodName The name of the method.
     * @param parameterClasses The list of parameters.
     * @return The Method object that matches the specified name and parameterTypes.
     */
    private static Method getMethod(final String fqcn, final String methodName, final Class<?>... parameterClasses) {
        try {
            final Class<?> type = getClass(fqcn);
            return type.getMethod(methodName, parameterClasses);
        } catch (Exception e) {
            throw new InstantiationException(
                String.format("Could not find %s#%s(): %s", fqcn, methodName, e), e);
        }
    }
}
