/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.exceptions.verification.ArgumentsAreDifferent;

public class ExceptionFactory {

    private ExceptionFactory() {
    }

    private static interface ExceptionFactoryImpl {
        AssertionError create(String message, String wanted, String actual);
    }

    private final static ExceptionFactoryImpl factory;

    static {
        ExceptionFactoryImpl theFactory = null;

        try {
            // The following is neater but requires -source and -target >= 1.8
            // theFactory = org.mockito.exceptions.verification.junit4.ArgumentsAreDifferent::new;
            theFactory = new ExceptionFactoryImpl() {
                @Override
                public AssertionError create(String message, String wanted, String actual) {
                    return new org.mockito.exceptions.verification.junit4.ArgumentsAreDifferent(message, wanted, actual);
                }
            };
        } catch (Throwable onlyIfJUnit4IsNotAvailable) {
            try {
                // The following is neater but requires -source and -target >= 1.8
                // theFactory = org.mockito.exceptions.verification.junit.ArgumentsAreDifferent::new;
                theFactory = new ExceptionFactoryImpl() {
                    @Override
                    public AssertionError create(String message, String wanted, String actual) {
                        return new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent(message, wanted, actual);
                    }
                };
            } catch (Throwable onlyIfJUnit3IsNotAvailable) {
            }
        }
        // The following is neater but requires -source and -target >= 1.8
        // factory = (theFactory == null) ? ArgumentsAreDifferent::new : theFactory;
        factory = (theFactory == null) ? new ExceptionFactoryImpl() {
            @Override
            public AssertionError create(String message, String wanted, String actual) {
                return new ArgumentsAreDifferent(message, wanted, actual);
            }
        } : theFactory;
    }

    /**
     * Returns an AssertionError that describes the fact that the arguments of an invocation are different.
     * If JUnit 4+ is on the class path, it returns a class that extends from JUnit's {@link org.junit.ComparisonFailure},
     * or else if JUnit 3 is on the class path it returns a class that extends from JUnit 3's
     * {@link junit.framework.ComparisonFailure}. This provides a better IDE support as the comparison result
     * can be opened in a visual diff.
     */
    public static AssertionError createArgumentsAreDifferentException(String message, String wanted, String actual) {
        return factory.create(message, wanted, actual);
    }
}
