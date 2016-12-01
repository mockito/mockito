package org.mockito.internal.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.mockito.exceptions.base.MockitoException;

final class EventHandler {

    private final Object listenerInstance;
    private final Method listenerMethod;
    private final Class<?> eventType;

    EventHandler(Object listenerInstance, Method listenerMethod) {
        this.listenerInstance = listenerInstance;
        this.listenerMethod = listenerMethod;
        this.eventType = listenerMethod.getParameterTypes()[0];
    }

    void tryHandleEvent(Object event) {
        if (!canHandle(event)) {
            return;
        }

        boolean isAccessible = listenerMethod.isAccessible();
        try {
            listenerMethod.setAccessible(true);
            
        } catch (SecurityException ignore) {
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
            listenerMethod.setAccessible(isAccessible);
        }
    }

    private boolean canHandle(Object event) {
        return eventType.isInstance(event);
    }

    private MockitoException internalBug(Exception e) {
        return new MockitoException("Ups this is a bug, this should never happen: " + e.getMessage() + "\r\n method: " + listenerMethod, e);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result;
        result = prime * listenerInstance.hashCode();
        result = prime * result +  listenerMethod.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventHandler other = (EventHandler) obj;
        if (listenerInstance == null) {
            if (other.listenerInstance != null)
                return false;
        } else if (!listenerInstance.equals(other.listenerInstance))
            return false;
        if (listenerMethod == null) {
            if (other.listenerMethod != null)
                return false;
        } else if (!listenerMethod.equals(other.listenerMethod))
            return false;
        return true;
    }
}