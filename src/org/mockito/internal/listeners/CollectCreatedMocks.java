/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CollectCreatedMocks implements MockingStartedListener {
    
    private final List toBeFilled;

    public CollectCreatedMocks(final List toBeFilled) {
        this.toBeFilled = toBeFilled;
    }

    public void mockingStarted(final Object mock, final Class classToMock) {
        toBeFilled.add(mock);
    }
}
