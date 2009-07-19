package org.mockito.internal.progress;

import static org.junit.Assert.*;

import org.junit.Test;


public class HandyReturnValuesTest {

    private HandyReturnValues h = new HandyReturnValues();

    @Test
    public void shouldNotReturnNullForPrimitivesWprappers() throws Exception {
        assertNotNull(h.returnFor(Boolean.class));
        assertNotNull(h.returnFor(Character.class));
        assertNotNull(h.returnFor(Byte.class));
        assertNotNull(h.returnFor(Short.class));
        assertNotNull(h.returnFor(Integer.class));
        assertNotNull(h.returnFor(Long.class));
        assertNotNull(h.returnFor(Float.class));
        assertNotNull(h.returnFor(Double.class));        
    }

    @Test
    public void shouldNotReturnNullForPrimitives() throws Exception {
        assertNotNull(h.returnFor(boolean.class));
        assertNotNull(h.returnFor(char.class));
        assertNotNull(h.returnFor(byte.class));
        assertNotNull(h.returnFor(short.class));
        assertNotNull(h.returnFor(int.class));
        assertNotNull(h.returnFor(long.class));
        assertNotNull(h.returnFor(float.class));
        assertNotNull(h.returnFor(double.class));
    }

    @Test
    public void shouldReturnNullForEverythingElse() throws Exception {
        assertNull(h.returnFor(Object.class));
        assertNull(h.returnFor(String.class));
    }
}
