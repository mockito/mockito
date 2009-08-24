package org.mockito.internal.util;

import org.mockitoutil.TestBase;
import static org.mockito.internal.util.ObjectMethodsGuru.*;
import org.mockitousage.IMethods;
import org.junit.Test;

public class ObjectMethodsGuruTest extends TestBase {

    ObjectMethodsGuru guru = new ObjectMethodsGuru();
    
    @Test
    public void shouldKnowToStringMethod() throws Exception {
        assertFalse(isToString(Object.class.getMethod("equals", Object.class)));
        assertFalse(isToString(IMethods.class.getMethod("toString", String.class)));
        assertTrue(isToString(IMethods.class.getMethod("toString")));
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
}
