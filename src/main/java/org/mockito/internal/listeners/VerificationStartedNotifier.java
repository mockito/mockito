package org.mockito.internal.listeners;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoMock;
import org.mockito.listeners.VerificationStartedEvent;
import org.mockito.listeners.VerificationStartedListener;

import java.util.List;

public class VerificationStartedNotifier {

    public static Object notifyVerificationStarted(List<VerificationStartedListener> listeners, MockitoMock originalMock) {
        if (listeners.isEmpty()) {
            return originalMock.getMock();
        }
        VerificationStartedEvent event = new Event(originalMock);
        for (VerificationStartedListener listener : listeners) {
            listener.onVerificationStarted(event);
        }
        return event.getMock();
    }

    static class Event implements VerificationStartedEvent {
        private final MockitoMock originalMock;
        private Object mock;

        public Event(MockitoMock originalMock) {
            this.originalMock = originalMock;
            this.mock = originalMock.getMock();
        }

        public void setMock(Object mock) {
            if (mock == null) {
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock", "null parameter");
            }
            MockitoMock mockitoMock = MockUtil.getMockitoMock(mock);
            if (!mockitoMock.isMock()) {
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock", "parameter which is not a Mockito mock");
                //TODO! use ValuePrinter to print the object that was passed (also add coverage)
                //if the user passed wrong argument, lets show him what argument was passed to streamline debugging
            }
            Class typeToMock = this.originalMock.getHandler().getMockSettings().getTypeToMock();
            if (!typeToMock.isInstance(mock)) {
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock",
                    "parameter which does not implement/extend the original mock type: " + typeToMock.getName());
            }
            this.mock = mock;
        }
        public Object getMock() {
            return mock;
        }
    }
}
