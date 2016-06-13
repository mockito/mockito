package org.mockito.internal.util;

import org.mockito.internal.creation.instance.InstantiationException;

import java.lang.reflect.Method;

/**
 * Helper class to work with features that were introduced in Java versions after 1.5.
 * This class uses reflection in most places to avoid coupling with a newer JDK.
 */
public final class JavaEightUtil {

    // No need for volatile, Optional#empty() is already a safe singleton.
    private static Object emptyOptional;

    private JavaEightUtil() {
        // utility class
    }

    /**
     * Creates an empty Optional using reflection to stay backwards-compatible with older
     * JDKs (see issue 191).
     *
     * @return an empty Optional.
     */
    public static Object emptyOptional() {
        // no need for double-checked locking
        if (emptyOptional != null) {
            return emptyOptional;
        }

        try {
            final Class<?> optionalClass = Class.forName("java.util.Optional");
            final Method emptyMethod = optionalClass.getMethod("empty");

            return emptyOptional = emptyMethod.invoke(null);
            // any exception is really unexpected since the type name has
            // already been verified to be java.util.Optional
        } catch (Exception e) {
            throw new InstantiationException("Could not create java.util.Optional#empty(): " + e, e);
        }
    }

    /**
     * Creates an empty Stream using reflection to stay backwards-compatible with older
     * JDKs.
     *
     * @return an empty Stream.
     */
    public static Object emptyStream() {
        // note: the empty stream can not be stored as a singleton.
        try {
            final Class<?> optionalClass = Class.forName("java.util.stream.Stream");
            final Method emptyMethod = optionalClass.getMethod("empty");

            return emptyMethod.invoke(null);
            // any exception is really unexpected since the type name has
            // already been verified
        } catch (Exception e) {
            throw new InstantiationException("Could not create java.util.stream.Stream#empty(): " + e, e);
        }
    }
}
