/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

public class MockName {
    
    private final String mockName;
    private boolean surrogate;

    @SuppressWarnings("unchecked")
    public MockName(String mockName, Class classToMock) {
        if (mockName == null) {
            this.mockName = toInstanceName(classToMock);
            this.surrogate = true;
        } else {
            this.mockName = mockName;
        }
    }

    private static String toInstanceName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        //lower case first letter
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
    
    public boolean isSurrogate() {
        return surrogate;
    }
    
    @Override
    public String toString() {
        return mockName;
    }
}
