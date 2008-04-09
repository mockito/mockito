/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure.withstaticutility;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.mockito.configuration.ReturnValues;
import org.mockito.configuration.experimental.ConfigurationSupport;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.invocation.InvocationOnMock;

public class AllowsFakingReturnValues {
    
    public static void fakeReturnValues(Object ... mocks) {
        FakeReturnValues fakeReturnValues = getFakeReturnValues();
        fakeReturnValues.addMocks(mocks);
    }
    
    private static FakeReturnValues getFakeReturnValues() {
        MockitoConfiguration config = ConfigurationSupport.getConfiguration();
        ReturnValues current = config.getReturnValues();
        //if my custom return values are NOT yet set, do it 
        if (!(current instanceof FakeReturnValues)) {
            config.setReturnValues(new FakeReturnValues());
        }
        return (FakeReturnValues) config.getReturnValues();
    }

    private static final class FakeReturnValues implements ReturnValues {
        
        private Set<Object> mocksReturningFakes = new HashSet<Object>();

        public Object valueFor(InvocationOnMock invocation) {
            if (mocksReturningFakes.contains(invocation.getMock())) {
                //return non-standard value only for 'special' mocks
                return returnFake(invocation);
            } else {
                //return default value for any other mock
                return ConfigurationSupport.defaultValueFor(invocation);
            }
        }

        public void addMocks(Object ... mocks) {
            mocksReturningFakes.addAll(Arrays.asList(mocks));
        }

        private Object returnFake(InvocationOnMock invocation) {
           
            Class<?> returnType = invocation.getMethod().getReturnType();
            
            Object defaultReturnValue = ConfigurationSupport.defaultValueFor(invocation);

            if (returnType == String.class) {
                return "";
            } else if (returnType == Boolean.TYPE) {
                return true;
            } else if (defaultReturnValue != null || !ConfigurationSupport.isMockable(returnType)) {
                return defaultReturnValue;
            } else {
                return mock(returnType);
            }
        }
    }
}
