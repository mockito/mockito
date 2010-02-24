package org.mockito.internal.listeners;

import org.mockito.MockSettings;

@SuppressWarnings("unchecked")
public interface MockingStartedListener extends MockingProgressListener {
    
    void mockingStarted(Object mock, Class classToMock, MockSettings mockSettings);
}
