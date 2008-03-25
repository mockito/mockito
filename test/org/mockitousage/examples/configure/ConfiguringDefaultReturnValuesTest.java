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
import org.mockito.TestBase;
import org.mockito.configuration.DefaultReturnValues;
import org.mockito.configuration.MockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.internal.invocation.InvocationOnMock;

public class ConfiguringDefaultReturnValuesTest extends TestBase {
    
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
    
    //Configuration code below is typically hidden in a base class / your test runner / some kind of static utility
    
    @Before
    public void configureDefaultReturnValues() {
        MockitoConfiguration.instance().setReturnValues(new MyDefaultReturnValues());
    }
    
    @After
    public void resetReturnValuesToDefaults() {
        MockitoConfiguration.instance().resetReturnValues();
    }
    
    private final class MyDefaultReturnValues implements ReturnValues {
        public Object valueFor(InvocationOnMock invocation) {
            //get the default return value
            Object value = new DefaultReturnValues().valueFor(invocation);
            if (value != null || invocation.getMethod().getReturnType() == Void.TYPE) {
                return value;
            } else {
                //in case the default return value is null and method is not void, return new mock:
                return Mockito.mock(invocation.getMethod().getReturnType());
            }
        }
    }
}