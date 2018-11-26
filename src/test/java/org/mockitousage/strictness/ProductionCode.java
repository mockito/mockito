/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.strictness;

import org.mockitousage.IMethods;

/**
 * Test utility class that simulates invocation of mock in production code.
 * In certain tests, the production code needs to be invoked in a different class/source file than the test.
 */
public class ProductionCode {

    public static void simpleMethod(IMethods mock, String argument) {
        mock.simpleMethod(argument);
    }

    public static void simpleMethod(IMethods mock, int argument) {
        mock.simpleMethod(argument);
    }
}
