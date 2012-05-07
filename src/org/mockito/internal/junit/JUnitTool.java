/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.exceptions.verification.ArgumentsAreDifferent;

public class JUnitTool {

    private static boolean hasJUnit;

    static {
        try {
            Class.forName("junit.framework.ComparisonFailure");
            hasJUnit = true;
        } catch (Throwable t) {
            hasJUnit = false;
        }
    }
    
    public static boolean hasJUnit() {
        return hasJUnit;
    }

    public static AssertionError createArgumentsAreDifferentException(String message, String wanted, String actual)  {
        try {
            Class<?> clazz = Class.forName("org.mockito.exceptions.verification.junit.ArgumentsAreDifferent");
            AssertionError throwable = (AssertionError) clazz.getConstructors()[0].newInstance(message, wanted, actual);
            return throwable;
        } catch (Throwable t) {
//            throw the default exception in case of problems
            return new ArgumentsAreDifferent(message);
        }
    }
}