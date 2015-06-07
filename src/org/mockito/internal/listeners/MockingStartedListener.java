/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;


public interface MockingStartedListener extends MockingProgressListener {
    
    void mockingStarted(final Object mock, final Class classToMock);
}
