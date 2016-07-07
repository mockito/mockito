/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

@SuppressWarnings("unchecked")
public interface MockingStartedListener extends MockingProgressListener {
    
    void mockingStarted(Object mock, Class<?> classToMock);
}
