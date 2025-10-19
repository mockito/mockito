package org.mockito.listeners;

import org.mockito.mock.MockCreationSettings;
import java.util.ArrayList;
import java.util.List;
import org.mockito.Mockito;

/**
 * Minimal reproduction for issue #3700: raw type warning in MockCreationListener.
 */
public class RawTypeWarningTest {

    private static final List<Object> ALL_MOCKS = new ArrayList<>();
    private static final MockCreationListener MOCK_CREATION_LISTENER = new MockCreationListenerImpl();

    public static void main(String[] args) {
        // just trigger the listener registration
        Mockito.framework().addListener(MOCK_CREATION_LISTENER);
        Mockito.framework().removeListener(MOCK_CREATION_LISTENER);
    }

    private static class MockCreationListenerImpl implements MockCreationListener {
        @Override
        public void onMockCreated(Object mock, MockCreationSettings settings) {
            ALL_MOCKS.add(mock);
        }

        @Override
        public void onStaticMockCreated(Class<?> mock, MockCreationSettings settings) {}
    }
}
