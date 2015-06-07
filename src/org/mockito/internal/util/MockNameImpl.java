/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.io.Serializable;

import org.mockito.mock.MockName;

@SuppressWarnings("rawtypes")
public class MockNameImpl implements MockName, Serializable {
    
    private static final long serialVersionUID = 8014974700844306925L;
    private final String mockName;
    private boolean defaultName;

    public MockNameImpl(final String mockName, final Class classToMock) {
        if (mockName == null) {
            this.mockName = toInstanceName(classToMock);
            this.defaultName = true;
        } else {
            this.mockName = mockName;
        }
    }

    public MockNameImpl(final String mockName) {
        this.mockName = mockName;
    }

    private static String toInstanceName(final Class<?> clazz) {
        String className = clazz.getSimpleName();
        if (className.length() == 0) {
            //it's an anonymous class, let's get name from the parent
            className = clazz.getSuperclass().getSimpleName();
        }
        //lower case first letter
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
    
    public boolean isDefault() {
        return defaultName;
    }
    
    @Override
    public String toString() {
        return mockName;
    }
}
