/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObjectMethodsGuruTest extends TestBase {


    private interface HasCompareToButDoesNotImplementComparable {
        int compareTo(HasCompareToButDoesNotImplementComparable other);
    }

    private interface HasCompare extends Comparable<HasCompare> {
        int foo(HasCompare other);
        int compareTo(HasCompare other, String redHerring);
        int compareTo(String redHerring);
        int compareTo(HasCompare redHerring);
    }

    @Test
    public void shouldKnowToStringMethod() throws Exception {
        assertFalse(ObjectMethodsGuru.isToStringMethod(Object.class.getMethod("equals", Object.class)));
        assertFalse(ObjectMethodsGuru.isToStringMethod(IMethods.class.getMethod("toString", String.class)));
        assertTrue(ObjectMethodsGuru.isToStringMethod(IMethods.class.getMethod("toString")));
    }


    @Test
    public void shouldKnowCompareToMethod() throws Exception {
        assertFalse(ObjectMethodsGuru.isCompareToMethod(Date.class.getMethod("toString")));
        assertFalse(ObjectMethodsGuru.isCompareToMethod(HasCompare.class.getMethod("foo", HasCompare.class)));
        assertFalse(ObjectMethodsGuru.isCompareToMethod(HasCompare.class.getMethod("compareTo", HasCompare.class, String.class)));
        assertFalse(ObjectMethodsGuru.isCompareToMethod(HasCompare.class.getMethod("compareTo", String.class)));
        assertFalse(ObjectMethodsGuru.isCompareToMethod(HasCompareToButDoesNotImplementComparable.class.getDeclaredMethod("compareTo", HasCompareToButDoesNotImplementComparable.class)));

        assertTrue(ObjectMethodsGuru.isCompareToMethod(HasCompare.class.getMethod("compareTo", HasCompare.class)));
    }
}
