/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

public class JUnitTool {

    private static final JUnitDetecter detecter = new JUnitDetecter();

    public static boolean hasJUnit() {
        return detecter.hasJUnit();
    }

    public static AssertionError createArgumentsAreDifferentException(String message, String wanted, String actual)  {
        return new FriendlyExceptionMaker(detecter).createArgumentsAreDifferentException(message, wanted, actual);
    }
}