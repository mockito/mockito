/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.examples.configure.withstaticutility;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.mockito.configuration.ConfigurationSupport;
import org.mockito.configuration.MockitoConfiguration;
import org.mockito.configuration.ReturnValues;
import org.mockito.invocation.InvocationOnMock;

public class AllowsFakingReturnValues {
    
    public static void fakeReturnValues(Object ... mocks) {
        FakeReturnValues fakeReturnValues = getFakeReturnValues();
        fakeReturnValues.addMocks(mocks);
    }
    
    private static FakeReturnValues getFakeReturnValues() {
        MockitoConfiguration config = MockitoConfiguration.instance();
        ReturnValues current = config.getReturnValues();
        //if my custom return values are NOT yet set, do it 
        if (!(current instanceof FakeReturnValues)) {
            config.setReturnValues(new FakeReturnValues());
        }
        return (FakeReturnValues) config.getReturnValues();
    }

    private static final class FakeReturnValues extends ConfigurationSupport implements ReturnValues {
        
        private Set<Object> mocksReturningFakes = new HashSet<Object>();

        public Object valueFor(InvocationOnMock invocation) {
            if (mocksReturningFakes.contains(invocation.getMock())) {
                return returnFake(invocation);
            } else {
                return defaultValueFor(invocation);
            }
        }

        public void addMocks(Object ... mocks) {
            mocksReturningFakes.addAll(Arrays.asList(mocks));
        }

        private Object returnFake(InvocationOnMock invocation) {
            Class<?> returnType = invocation.getMethod().getReturnType();
            if (returnType == String.class) {
                return "";
            } else if (returnType == Boolean.TYPE) {
                return true;
            } else if (!isMockable(returnType)) {
                return mock(returnType);
            } else {
                return defaultValueFor(invocation);
            }
        }
    }
}
