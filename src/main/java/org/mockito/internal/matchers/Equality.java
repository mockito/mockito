/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.lang.reflect.Array;

//stolen from hamcrest because I didn't want to have more dependency than Matcher class 
public class Equality {

    public static boolean areEqual(Object o1, Object o2) {
        if (o1 == o2 ) {
            return true;
    } else if (o1 == null || o2 == null) {
            return false;
        } else if (isArray(o1)) {
            return isArray(o2) && areArraysEqual(o1, o2);
        } else {
            return o1.equals(o2);
        }
    }

    static boolean areArraysEqual(Object o1, Object o2) {
        return areArrayLengthsEqual(o1, o2)
                && areArrayElementsEqual(o1, o2);
    }

    static boolean areArrayLengthsEqual(Object o1, Object o2) {
        return Array.getLength(o1) == Array.getLength(o2);
    }

    static boolean areArrayElementsEqual(Object o1, Object o2) {
        for (int i = 0; i < Array.getLength(o1); i++) {
            if (!areEqual(Array.get(o1, i), Array.get(o2, i))) return false;
        }
        return true;
    }

    static boolean isArray(Object o) {
        return o.getClass().isArray();
    }
}