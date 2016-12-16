package org.mockito.internal.util.eventbus;

import static java.lang.System.identityHashCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.mockito.exceptions.base.MockitoException;

public class Subscriber {

    private final Object listenerInstance;
    private final Method listenerMethod;
    private final Class<?> eventType;

    public Subscriber(Object listenerInstance, Method listenerMethod) {
        this.listenerInstance = listenerInstance;
        this.listenerMethod = listenerMethod;
        this.eventType = listenerMethod.getParameterTypes()[0];
    }

    public final void tryHandleEvent(Object event) {
        if (canHandle(event)) {
            invokeListenerMethod(event);
        }
    }

    protected void invokeListenerMethod(Object event) {
        Boolean initialState = listenerMethod.isAccessible();
        try {
            listenerMethod.setAccessible(true);
        } catch (SecurityException ignore) {
            initialState = null;// null means the accessibility was not changed
        }

        try {
            listenerMethod.invoke(listenerInstance, event);
        } catch (IllegalAccessException e) {
            throw internalBug(e);
        } catch (IllegalArgumentException e) {
            throw internalBug(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (initialState != null) {
                listenerMethod.setAccessible(initialState);
            }
        }
    }

    private boolean canHandle(Object event) {
        return eventType.isInstance(event);
    }

    private MockitoException internalBug(Exception e) {
        return new MockitoException("Ups this is a bug, this should never happen: " + e.getMessage() + "\r\n method: " + listenerMethod, e);
    }

    @Override
    public final int hashCode() {
        return (31 + identityHashCode(listenerInstance)) * 31 + listenerMethod.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Subscriber) {
            Subscriber that = (Subscriber) obj;
            // Use == so that different equal instances will still receive events.
            // We only guard against the case that the same object is registered
            // multiple times
            return listenerInstance == that.listenerInstance && listenerMethod.equals(that.listenerMethod);
        }
        return false;
    }

    public final Object getListener() {
        return listenerInstance;
    }
}