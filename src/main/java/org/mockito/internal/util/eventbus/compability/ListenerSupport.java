package org.mockito.internal.util.eventbus.compability;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.eventbus.Subscriber;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;
import org.mockito.listeners.MockCreatedReport;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

public class ListenerSupport {

    private ListenerSupport() {
    }

    public static Set<Subscriber> legacyListenerOf(Object listener) {
        Set<Subscriber> s = new HashSet<Subscriber>();
        if (listener instanceof MockCreationListener) {
            s.add(subscriberOf((MockCreationListener) listener));
        }
        if (listener instanceof InvocationListener) {
            s.add(subscriberOf((InvocationListener) listener));

        }
        return s;
    }

    private static Subscriber subscriberOf(final InvocationListener listener) {
        Class<InvocationListener> type = InvocationListener.class;
        Method method;
        try {
            method = type.getMethod("reportInvocation", MethodInvocationReport.class);
        } catch (Exception e) {
            throw new MockitoException("Ups this is a bug: " + e.getMessage(), e);
        }
        return new Subscriber(listener, method) {
            @Override
            protected void invokeListenerMethod(Object event) {
                listener.reportInvocation((MethodInvocationReport) event);
            };
        };
    }

    private static Subscriber subscriberOf(final MockCreationListener listener) {
        Class<MockCreationListener> type = MockCreationListener.class;
        Method method;
        try {
            method = type.getMethod("onMockCreated", Object.class, MockCreationSettings.class);
        } catch (Exception e) {
            throw new MockitoException("Ups this is a bug: " + e.getMessage(), e);
        }

        return new Subscriber(listener, method) {
            @Override
            protected void invokeListenerMethod(Object event) {
                MockCreatedReport r = (MockCreatedReport) event;

                listener.onMockCreated(r.getMock(), r.getMockCreationSettings());
            }
        };
    }
}
