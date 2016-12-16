package org.mockito.listeners;

import org.mockito.mock.MockCreationSettings;

public class MockCreatedReport {

    private final Object mock;
    private final MockCreationSettings<?> settings;

    public MockCreatedReport(Object mock, MockCreationSettings<?> settings) {
        this.mock = mock;
        this.settings = settings;
    }

    public Object getMock() {
        return mock;
    }

    public MockCreationSettings<?> getMockCreationSettings() {
        return settings;
    }

}
