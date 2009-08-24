package org.mockito.internal.util;

import org.mockitoutil.TestBase;
import static org.mockito.internal.util.ObjectMethodsGuru.*;
import org.mockitousage.IMethods;
import org.junit.Test;

public class ObjectMethodsGuruTest extends TestBase {
    
    @Test
    public void shouldKnowToStringMethod() throws Exception {
        assertFalse(isToString(String.class.getMethod("equals", Object.class)));
        assertFalse(isToString(IMethods.class.getMethod("toString", String.class)));
        assertTrue(isToString(Object.class.getMethod("toString")));
    }
}
