/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure.withbaseclass;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ConfiguringDefaultReturnValuesUsingBaseClassTest extends MakesMocksNotToReturnNulls {
    
    @Test
    public void shouldReturnMocksByDefaultInsteadOfNulls() throws Exception {
        MyObject m = mock(MyObject.class);
        //mocks don't return nulls any more...
        MyObject returned = m.foo();
        assertNotNull(returned);
        assertNotNull(returned.foo());
        
        assertEquals(0, returned.bar());
    }

    interface MyObject {
        MyObject foo();
        int bar();
    }
}