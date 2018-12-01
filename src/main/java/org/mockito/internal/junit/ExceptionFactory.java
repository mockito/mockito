/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import junit.framework.ComparisonFailure;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;

public class ExceptionFactory {

    private final static boolean hasJUnit = canLoadJunitClass();

    private ExceptionFactory() {
    }

    /**
     * If JUnit is used, an AssertionError is returned that extends from JUnit {@link ComparisonFailure} and hence provide a better IDE support as the comparison result is comparable
     */
    public static AssertionError createArgumentsAreDifferentException(String message, String wanted, String actual) {
        if (hasJUnit) {
            return createJUnitArgumentsAreDifferent(message, wanted, actual);
        }
        return new ArgumentsAreDifferent(message);
    }

    private static AssertionError createJUnitArgumentsAreDifferent(String message, String wanted, String actual) {
        return JUnitArgsAreDifferent.create(message, wanted, actual);
    }

    private static boolean canLoadJunitClass() {
        try {
            JUnitArgsAreDifferent.create("message", "wanted", "actual");
        } catch (Throwable onlyIfJUnitIsNotAvailable) {
            return false;
        }
        return true;
    }

    /**
     * Don't inline this class! It allows create the JUnit-ArgumentsAreDifferent exception without the need to use reflection.
     * <p>
     * If JUnit is not available a call to {@link #create(String, String, String)} will throw a {@link NoClassDefFoundError}.
     * The {@link NoClassDefFoundError} will be thrown by the class loader cause the JUnit class {@link ComparisonFailure}
     * can't be loaded which is a upper class of ArgumentsAreDifferent.
     */
    private static class JUnitArgsAreDifferent {
        static AssertionError create(String message, String wanted, String actual) {
            return new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent(message, wanted, actual);
        }
    }
}
