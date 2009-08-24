package org.mockito.internal.util;

import org.mockitoutil.TestBase;
import static org.mockito.internal.util.ObjectMethodsGuru.*;
import org.mockitousage.IMethods;
import org.junit.Test;

public class ObjectMethodsGuruTest extends TestBase {

    ObjectMethodsGuru guru = new ObjectMethodsGuru();
    
    @Test
    public void shouldKnowToStringMethod() throws Exception {
        assertFalse(isToString(String.class.getMethod("equals", Object.class)));
        assertFalse(isToString(IMethods.class.getMethod("toString", String.class)));
        assertTrue(isToString(Object.class.getMethod("toString")));
    }

    @Test
    public void shouldKnowEqualsMethod() throws Exception {
        assertFalse(guru.isEqualsMethod(IMethods.class.getMethod("equals", String.class)));
        assertFalse(guru.isEqualsMethod(Object.class.getMethod("toString")));
        assertTrue(guru.isEqualsMethod(String.class.getMethod("equals", Object.class)));
    }
}
