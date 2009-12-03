/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.junit.Test;
import org.mockitoutil.TestBase;


public class HandyReturnValuesTest extends TestBase {

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
        assertNull(h.returnFor(null));
    }

    @Test
    public void shouldReturnHandyValueForInstances() throws Exception {
        assertNull(h.returnFor(new Object()));
        assertNull(h.returnFor((Object) null));
        
        assertNotNull(h.returnFor(10.0));
        assertNotNull(h.returnFor(Boolean.FALSE));
    }
}
