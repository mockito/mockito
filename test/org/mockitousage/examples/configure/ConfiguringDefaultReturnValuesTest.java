/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ConfiguringDefaultReturnValuesTest extends MakesMocksNotToReturnNulls {
    
    @Test
    public void shouldReturnMocksByDefaultInsteadOfNulls() throws Exception {
        MyObject m = mock(MyObject.class);
        //mocks don't return nulls any more...
        MyObject returned = m.foo();
        assertNotNull(returned);
        assertNotNull(returned.foo());
    }

    interface MyObject {
        MyObject foo();
    }
}