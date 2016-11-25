/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.internal.creation.instance.InstantiationException;

import java.lang.reflect.Method;

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
     * Invokes a nullary static factory method using reflection to stay backwards-compatible with older JDKs.
     *
     * @param fqcn The fully qualified class name of the type to be produced.
     * @param methodName The name of the factory method.
     * @return the object produced.
     */
    private static Object invokeNullaryFactoryMethod(final String fqcn, final String methodName) {
        try {
            final Class<?> type = Class.forName(fqcn);
            final Method method = type.getMethod(methodName);

            return method.invoke(null);
            // any exception is really unexpected since the type name has
            // already been verified
        } catch (final Exception e) {
            throw new InstantiationException(
                    String.format("Could not create %s#%s(): %s", fqcn, methodName, e), e);
        }
    }
}
