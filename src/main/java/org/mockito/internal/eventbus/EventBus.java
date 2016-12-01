package org.mockito.internal.eventbus;

import static org.mockito.internal.util.Checks.checkNotNull;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class EventBus {

    private final Set<EventHandler> eventHandlers = new HashSet<EventHandler>();

    public void register(Object listener) {
        checkNotNull(listener, "The listener must not be null!");

        eventHandlers.addAll(eventHandlerOf(listener));
    }

    public void post(Object event) {
        checkNotNull(event, "The event must not be null!");

        for (EventHandler eventHandler : eventHandlers) {
            eventHandler.tryHandleEvent(event);
        }
    }

    private static Set<EventHandler> eventHandlerOf(Object listener) {

        Set<EventHandler> result = new HashSet<EventHandler>();
        for (Method method : listener.getClass().getMethods()) {

            if (isEventHandlerMethod(method)) {
                result.add(new EventHandler(listener, method));
            }
        }

        return result;
    }

    private static boolean isEventHandlerMethod(Method method) {
        if (!method.isAnnotationPresent(Subscribe.class)) {
            return false;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            return false;
        }

        Class<?> eventType = parameterTypes[0];

        if (eventType.isPrimitive()) {
            return false;
        }

        return true;
    }
}