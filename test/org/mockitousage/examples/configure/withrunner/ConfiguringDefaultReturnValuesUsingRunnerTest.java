/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure.withrunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MakesMocksNotToReturnNulls.class)
public class ConfiguringDefaultReturnValuesUsingRunnerTest {
    
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