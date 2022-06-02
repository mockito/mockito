/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.util.function.Predicate;

public class ProductionCode {

    @SuppressWarnings("ReturnValueIgnored")
    public static void simpleMethod(Predicate<String> mock, String argument) {
        mock.test(argument);
    }
}
