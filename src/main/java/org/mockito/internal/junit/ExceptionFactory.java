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
            // The following is neater but requires -source >= 1.8
            // theFactory = org.mockito.exceptions.verification.junit.ArgumentsAreDifferent::new;
            theFactory = new ExceptionFactoryImpl() {
                @Override
                public AssertionError create(String message, String wanted, String actual) {
                    return new org.mockito.exceptions.verification.junit.ArgumentsAreDifferent(message, wanted, actual);
                }
            };
        } catch (Throwable onlyIfJUnit3IsNotAvailable) {
        }
        // The following is much neater but requires -source >= 1.8
        // factory = (theFactory == null) ? ArgumentsAreDifferent::new : theFactory;
        factory = (theFactory == null) ? new ExceptionFactoryImpl() {
            @Override
            public AssertionError create(String message, String wanted, String actual) {
                return new ArgumentsAreDifferent(message, wanted, actual);
            }
        } : theFactory;
    }
    
    /**
     * If JUnit is used, an AssertionError is returned that extends from JUnit {@link ComparisonFailure} and hence provide a better IDE support as the comparison result is comparable
     */
    public static AssertionError createArgumentsAreDifferentException(String message, String wanted, String actual) {
        return factory.create(message, wanted, actual);
    }
}
