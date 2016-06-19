/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import java.util.List;

@SuppressWarnings("unchecked")
public class CollectCreatedMocks implements MockingStartedListener {
    
    private final List<Object> toBeFilled;

    public CollectCreatedMocks(List<Object> toBeFilled) {
        this.toBeFilled = toBeFilled;
    }

    public void mockingStarted(Object mock, Class<?> classToMock) {
        toBeFilled.add(mock);
    }
}
