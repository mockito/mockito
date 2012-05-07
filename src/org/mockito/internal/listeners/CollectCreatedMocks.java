/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import org.mockito.MockSettings;

import java.util.List;

@SuppressWarnings("unchecked")
public class CollectCreatedMocks implements MockingStartedListener {
    
    private List toBeFilled;

    public CollectCreatedMocks(List toBeFilled) {
        this.toBeFilled = toBeFilled;
    }

    public void mockingStarted(Object mock, Class classToMock) {
        toBeFilled.add(mock);
    }
}
