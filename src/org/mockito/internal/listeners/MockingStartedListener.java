package org.mockito.internal.listeners;

import org.mockito.MockSettings;

public interface MockingStartedListener extends MockingProgressListener {
    void mockingStarted(Object mock, Class classToMock, MockSettings mockSettings);
}
