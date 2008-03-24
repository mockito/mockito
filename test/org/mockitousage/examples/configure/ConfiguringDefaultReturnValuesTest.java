/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoConfiguration;
import org.mockito.ReturnValues;
import org.mockito.TestBase;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.DefaultReturnValues;

public class ConfiguringDefaultReturnValuesTest extends TestBase {
    
    @Test
    public void shouldReturnMocksByDefaultInsteadOfNulls() throws Exception {
        MyObject m = mock(MyObject.class);
        MyObject returned = m.foo();
        assertNotNull(returned);
        assertNotNull(returned.foo());
    }

    interface MyObject {
        MyObject foo();
    }
    
    @Before
    public void configureDefaultReturnValues() {
        MockitoConfiguration.setCustomReturnValues(new MyDefaultReturnValues());
    }
    
    @After
    public void resetDefaultReturnValues() {
        MockitoConfiguration.resetCustomReturnValues();
        
    }
    private final class MyDefaultReturnValues implements ReturnValues {
        public Object valueFor(Invocation invocation) {
            Object value = new DefaultReturnValues().valueFor(invocation);
            if (value != null || invocation.getMethod().getReturnType() == Void.TYPE) {
                return value;
            } else {
                return Mockito.mock(invocation.getMethod().getReturnType());
            }
        }
    }
}