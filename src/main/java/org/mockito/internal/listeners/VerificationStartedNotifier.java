package org.mockito.internal.listeners;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.matchers.text.ValuePrinter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoMock;
import org.mockito.listeners.VerificationStartedEvent;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;

import java.util.List;
import java.util.Set;

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
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock", "null parameter.");
            }
            MockitoMock mockitoMock = MockUtil.getMockitoMock(mock);
            if (!mockitoMock.isMock()) {
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock", "parameter which is not a Mockito mock.");
                //TODO! use ValuePrinter to print the object that was passed (also add coverage)
                //if the user passed wrong argument, lets show him what argument was passed to streamline debugging
            }
            MockCreationSettings originalMockSettings = this.originalMock.getHandler().getMockSettings();
            assertCompatibleTypes(mock, originalMockSettings);
            this.mock = mock;
        }
        public Object getMock() {
            return mock;
        }
    }

    static void assertCompatibleTypes(Object mock, MockCreationSettings originalSettings) {
        Class originalType = originalSettings.getTypeToMock();
        if (!originalType.isInstance(mock)) {
            throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock",
                "parameter which is not the same type as the original mock.\n" +
                    "  Required type: " + originalType.getName() + "\n" +
                    "  Received parameter: " + ValuePrinter.print(mock) + ".\n ");
        }

        for (Class iface : (Set<Class>) originalSettings.getExtraInterfaces()) {
            if (!iface.isInstance(mock)) {
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock",
                    "parameter which does not implement all extra interfaces of the original mock.\n" +
                        "  Required type: " + originalType.getName() + "\n" +
                        "  Required extra interface: " + iface.getName() + "\n" +
                        "  Received parameter: " + ValuePrinter.print(mock) + ".\n ");

            }
        }
    }
}
