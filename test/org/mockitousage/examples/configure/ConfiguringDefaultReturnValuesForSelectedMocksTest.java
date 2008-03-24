/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.configuration.DefaultReturnValues;
import org.mockito.configuration.MockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.internal.invocation.Invocation;

public class ConfiguringDefaultReturnValuesForSelectedMocksTest extends TestBase {
    
    private MyDefaultReturnValues myDefaultReturnValues;

    @Test
    public void shouldAllowConfiguringReturnValuesForSelectedMocks() throws Exception {
        MyObject smartStub = mock(MyObject.class);
        beSmartStub(smartStub);
        MyObject mock = mock(MyObject.class);
        
        assertNotNull(smartStub.returnMyObject());
        assertEquals("", smartStub.returnString());
        assertTrue(smartStub.returnBoolean());
        
        assertNull(mock.returnMyObject());
        assertNull(mock.returnString());
        assertFalse(mock.returnBoolean());
    }

    interface MyObject {
        MyObject returnMyObject();
        String returnString();
        boolean returnBoolean();
    }
    
    private void beSmartStub(Object mock) {
        myDefaultReturnValues.addSmartStub(mock);
    }
    
    @Before
    public void configureDefaultReturnValues() {
        myDefaultReturnValues = new MyDefaultReturnValues();
        MockitoConfiguration.setCustomReturnValues(myDefaultReturnValues);
    }
    
    @After
    public void resetDefaultReturnValues() {
        MockitoConfiguration.resetCustomReturnValues();
    }
    
    private final class MyDefaultReturnValues implements ReturnValues {
        private List<Object> smartStubs = new LinkedList<Object>();

        public Object valueFor(Invocation invocation) {
            Object value = new DefaultReturnValues().valueFor(invocation);
            Class<?> returnType = invocation.getMethod().getReturnType();
            if (value != null || returnType == Void.TYPE) {
                return value;
            } else if (smartStubs.contains(invocation.getMock())) {
                return returnValueForSmartStub(returnType);
            } else {
                return null;
            }
        }

        private Object returnValueForSmartStub(Class<?> returnType) {
            if (returnType == String.class) {
                return "";
            } else if (returnType == Boolean.TYPE) {
                return true;
            } else {
                return mock(returnType);
            }
        }

        public void addSmartStub(Object mock) {
            smartStubs.add(mock);
        }
    }
}