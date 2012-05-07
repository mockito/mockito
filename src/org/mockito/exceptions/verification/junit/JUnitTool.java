/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.verification.junit;

@Deprecated
/**
 * @Deprecated. This class has been moved to internal packages because it was never meant to be public.
 * If you need it for extending Mockito please let us know. You can still use {@link org.mockito.internal.junit.JUnitTool}.
 * However, the package clearly states that the class in a part of a public API so it can change.
 */
public class JUnitTool {
    
    public static boolean hasJUnit() {
        return org.mockito.internal.junit.JUnitTool.hasJUnit();
    }

    public static AssertionError createArgumentsAreDifferentException(String message, String wanted, String actual)  {
        return org.mockito.internal.junit.JUnitTool.createArgumentsAreDifferentException(message, wanted, actual);
    }
}