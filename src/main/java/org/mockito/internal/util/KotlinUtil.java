package org.mockito.internal.util;

import java.lang.annotation.Annotation;

/**
 * Provides the utilities for the Kotlin language.
 */
public final class KotlinUtil {

    private KotlinUtil() {
    }

    /**
     * Tests weather the given class is compiled by the Kotlin compiler.
     *
     * @param c the class
     * @return {@code true} if the given class is compiled by the Kotlin compiler; {@code false} otherwise
     */
    public static boolean isOfKotlin(Class<?> c) {
        for (Annotation a : c.getDeclaredAnnotations()) {
            // All the Kotlin classes are marked with the `kotlin.Metadata` annotation.
            if (a.annotationType().getName().equals("kotlin.Metadata")) {
                return true;
            }
        }
        return false;
    }

}
