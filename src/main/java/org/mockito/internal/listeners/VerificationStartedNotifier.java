package org.mockito.internal.listeners;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;
import org.mockito.listeners.VerificationStartedEvent;
import org.mockito.listeners.VerificationStartedListener;

import java.util.List;

public class VerificationStartedNotifier {

    public static Object notifyVerificationStarted(List<VerificationStartedListener> listeners, Object mock) {
        if (listeners.isEmpty()) {
            return mock;
        }
        VerificationStartedEvent event = new Event();
        event.setMock(mock);
        for (VerificationStartedListener listener : listeners) {
            listener.onVerificationStarted(event);
        }
        return event.getMock();
    }

    static class Event implements VerificationStartedEvent {
        private Object mock;
        public void setMock(Object mock) {
            if (mock == null) {
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock", "null parameter");
            }
            if (!MockUtil.isMock(mock)) {
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock", "parameter which is not a Mockito mock");
                //TODO! use ValuePrinter to print the object that was passed
                //if the user passed wrong argument, lets show him what argument was passed to streamline debugging
            }
            this.mock = mock;
        }
        public Object getMock() {
            return mock;
        }
    }
}
