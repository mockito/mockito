package org.mockito.internal.listeners;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.listeners.VerificationStartedEvent;
import org.mockito.listeners.VerificationStartedListener;

import java.util.List;

public class VerificationStartedNotifier {

    public static Object notifyVerificationStarted(List<VerificationStartedListener> listeners, Object mock) {
        if (listeners.isEmpty()) {
            return mock;
        }
        VerificationStartedEvent event = new DefaultVerificationStartedEvent();
        event.setMock(mock);
        for (VerificationStartedListener listener : listeners) {
            listener.onVerificationStarted(event);
        }
        return event.getMock();
    }

    private static class DefaultVerificationStartedEvent implements VerificationStartedEvent {
        private Object mock;
        public void setMock(Object mock) {
            if (mock == null) {
                throw new MockitoException("Null passed to VerificationStartedEvent.setMock() method.\n" +
                    "Null is not acceptable, see Javadoc for VerificationStartedListener for API information.");
            }
            this.mock = mock;
        }
        public Object getMock() {
            return mock;
        }
    }
}
