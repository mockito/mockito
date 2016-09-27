/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

public class JUnitTool {

    private static final JUnitDetector detector = new JUnitDetector();

    public static boolean hasJUnit() {
        return detector.hasJUnit();
    }

    public static AssertionError createArgumentsAreDifferentException(String message, String wanted, String actual)  {
        return new FriendlyExceptionMaker(detector).createArgumentsAreDifferentException(message, wanted, actual);
    }
}