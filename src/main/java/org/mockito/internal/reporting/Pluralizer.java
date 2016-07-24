/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

public class Pluralizer {

    public static String pluralize(int number) {
        return number == 1 ? "1 time" : number + " times";
    }

    public static String were_exactly_x_interactions(int x) {
        if (x == 1) {
            return "was exactly 1 interaction";
        } else {
            return "were exactly " + x + " interactions";
        }
    }
}
