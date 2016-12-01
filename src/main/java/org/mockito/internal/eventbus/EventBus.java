package org.mockito.internal.eventbus;

import static java.lang.reflect.Modifier.isPrivate;
import static org.mockito.internal.util.Checks.checkNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.mockito.exceptions.base.MockitoException;

public class EventBus {

    private final Set<EventHandler> handler = new HashSet<EventHandler>();

    public void register(Object listener) {
        checkNotNull(listener, "The listener must not be null!");
        checkNotPrivate(listener);
        Set<EventHandler> handlers = getEventHandler(listener);

        handler.addAll(handlers);
    }

    public void post(Object event) {
        checkNotNull(event, "The event must not be null!");

        for (EventHandler eventHandler : handler) {
            eventHandler.tryHandleEvent(event);
        }
    }

    private Set<EventHandler> getEventHandler(Object listener) {

        Set<EventHandler> result = new HashSet<EventHandler>();
        for (Method method : listener.getClass().getMethods()) {
            EventHandler h = getPossibleEventHandler(listener, method);
            if (h != null) {
                result.add(h);
            }
        }

        return result;
    }

    private EventHandler getPossibleEventHandler(Object listener, Method method) {
        if (!method.isAnnotationPresent(Subscribe.class)) {
            return null;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length!=1){
            return null;
        }
        
        
        Class<?> eventType = parameterTypes[0];
        
        if (eventType.isPrimitive()){
            return null;
        }
            
        return new EventHandler(listener, method, eventType);
    }

    private void checkNotPrivate(Object listener) {
        Class<?> type = listener.getClass();
        if (isPrivate(type.getModifiers())) {
            throw new IllegalArgumentException("The visibility of the listener class must be public! Got: "+type);
        }
    }

    private final static class EventHandler {

        private final Object listenerInstance;
        private final Method listenerMethod;
        private final Class<?> eventType;

        EventHandler(Object listenerInstance, Method listenerMethod, Class<?> eventType) {
            this.listenerInstance = listenerInstance;
            this.listenerMethod = listenerMethod;
            this.eventType = eventType;
        }

        void tryHandleEvent(Object event) {
            if (!eventType.isInstance(event)) {
                return;
            }

            try {
                listenerMethod.invoke(listenerInstance, event);
            } catch (IllegalAccessException e) {
                throw internalBug(e);
            } catch (IllegalArgumentException e) {
                throw internalBug(e);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        private MockitoException internalBug(Exception e) {
            return new MockitoException("Ups this is a bug, this should never happen: " + e.getMessage() + "\r\n method: " + listenerMethod, e);
        }

        @Override
        public String toString() {
            return "EventHandler[" + listenerMethod + "]";
        }
    }
}
