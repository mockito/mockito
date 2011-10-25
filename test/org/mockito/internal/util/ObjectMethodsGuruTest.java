/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.Date;

public class ObjectMethodsGuruTest extends TestBase {

    ObjectMethodsGuru guru = new ObjectMethodsGuru();
    
    @Test
    public void shouldKnowToStringMethod() throws Exception {
        assertFalse(guru.isToString(Object.class.getMethod("equals", Object.class)));
        assertFalse(guru.isToString(IMethods.class.getMethod("toString", String.class)));
        assertTrue(guru.isToString(IMethods.class.getMethod("toString")));
    }

    @Test
    public void shouldKnowEqualsMethod() throws Exception {
        assertFalse(guru.isEqualsMethod(IMethods.class.getMethod("equals", String.class)));
        assertFalse(guru.isEqualsMethod(IMethods.class.getMethod("equals")));
        assertFalse(guru.isEqualsMethod(Object.class.getMethod("toString")));
        assertTrue(guru.isEqualsMethod(Object.class.getMethod("equals", Object.class)));
    }

    @Test
    public void shouldKnowHashCodeMethod() throws Exception {
        assertFalse(guru.isHashCodeMethod(IMethods.class.getMethod("toString")));
        assertFalse(guru.isHashCodeMethod(IMethods.class.getMethod("hashCode", String.class)));
        assertTrue(guru.isHashCodeMethod(Object.class.getDeclaredMethod("hashCode")));
    }

    interface HasCompareToButDoesNotImplementComparable {
        public int compareTo(HasCompareToButDoesNotImplementComparable other);
    }

    interface HasCompare extends Comparable {
        public int foo(HasCompare other);
        public int compareTo(HasCompare other, String redHerring);
        public int compareTo(String redHerring);
        public int compareTo(HasCompare redHerring);
    }

    @Test
    public void shouldKnowCompareToMethod() throws Exception {
        assertFalse(guru.isCompareToMethod(Date.class.getMethod("toString")));
        assertFalse(guru.isCompareToMethod(HasCompare.class.getMethod("foo", HasCompare.class)));
        assertFalse(guru.isCompareToMethod(HasCompare.class.getMethod("compareTo", HasCompare.class, String.class)));
        assertFalse(guru.isCompareToMethod(HasCompare.class.getMethod("compareTo", String.class)));
        assertFalse(guru.isCompareToMethod(HasCompareToButDoesNotImplementComparable.class.getDeclaredMethod("compareTo", HasCompareToButDoesNotImplementComparable.class)));

        assertTrue(guru.isCompareToMethod(HasCompare.class.getMethod("compareTo", HasCompare.class)));
    }
}
