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

        return emptyOptionalDouble =
                invokeNullaryFactoryMethod("java.util.OptionalDouble", "empty");
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
     * Gets a value of the classes' field using reflection to stay backwards-compatible with older JDKs.
     *
     * @param fqcn The fully qualified class name of the type to be produced.
     * @param fieldName The name of th classes' field which value is going to be returned.
     * @return the restored value.
     */
    private static Object getStaticFieldValue(final String fqcn, final String fieldName) {
        try {
            final Class<?> type = getClass(fqcn);
            final Field field = type.getField(fieldName);
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
            throw new InstantiationException(String.format("Could not find %s: %s", fqcn, e), e);
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
    private static Method getMethod(
            final String fqcn, final String methodName, final Class<?>... parameterClasses) {
        try {
            final Class<?> type = getClass(fqcn);
            return type.getMethod(methodName, parameterClasses);
        } catch (Exception e) {
            throw new InstantiationException(
                    String.format("Could not find %s#%s(): %s", fqcn, methodName, e), e);
        }
    }
}
