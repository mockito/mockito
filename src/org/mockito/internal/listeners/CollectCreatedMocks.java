package org.mockito.internal.listeners;

import org.mockito.MockSettings;

import java.util.List;

public class CollectCreatedMocks implements MockingStartedListener {
    private List toBeFilled;

    public CollectCreatedMocks(List toBeFilled) {
        this.toBeFilled = toBeFilled;
    }

    public void mockingStarted(Object mock, Class classToMock, MockSettings mockSettings) {
        toBeFilled.add(mock);
    }
}
