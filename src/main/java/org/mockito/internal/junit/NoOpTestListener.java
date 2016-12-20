package org.mockito.internal.junit;

import org.mockito.mock.MockCreationSettings;

public class NoOpTestListener implements MockitoTestListener {

    public void testFinished(TestFinishedEvent event) {}

    public void onMockCreated(Object mock, MockCreationSettings settings) {}
}
