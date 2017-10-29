/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.matchers.text.ValuePrinter;
import org.mockito.listeners.VerificationStartedEvent;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;

import java.util.List;
import java.util.Set;

public class VerificationStartedNotifier {

    public static Object notifyVerificationStarted(List<VerificationStartedListener> listeners, MockingDetails originalMockingDetails) {
        if (listeners.isEmpty()) {
            return originalMockingDetails.getMock();
        }
        VerificationStartedEvent event = new Event(originalMockingDetails);
        for (VerificationStartedListener listener : listeners) {
            listener.onVerificationStarted(event);
        }
        return event.getMock();
    }

    static class Event implements VerificationStartedEvent {
        private final MockingDetails originalMockingDetails;
        private Object mock;

        public Event(MockingDetails originalMockingDetails) {
            this.originalMockingDetails = originalMockingDetails;
            this.mock = originalMockingDetails.getMock();
        }

        public void setMock(Object mock) {
            if (mock == null) {
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock", "null parameter.");
            }
            MockingDetails mockingDetails = Mockito.mockingDetails(mock);
            if (!mockingDetails.isMock()) {
                throw Reporter.methodDoesNotAcceptParameter("VerificationStartedEvent.setMock", "parameter which is not a Mockito mock.\n" +
                    "  Received parameter: " + ValuePrinter.print(mock) + ".\n ");
            }
            MockCreationSettings originalMockSettings = this.originalMockingDetails.getMockCreationSettings();
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
