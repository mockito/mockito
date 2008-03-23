package org.mockito;

import org.mockito.internal.creation.MockNamer;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.EmptyReturnValues;

public class EmptyValuesProvider {
    
    public <T> Object valueFor(Invocation invocation) {
        if (invocation.isToString()) {
            Object mock = invocation.getMock();
            String mockDescription = "Mock for " + MockNamer.nameForMock(mock) + ", hashCode: " + mock.hashCode();
            return mockDescription;
        }
        
        Class<?> returnType = invocation.getMethod().getReturnType();
        return EmptyReturnValues.emptyValueFor(returnType);
    }
}