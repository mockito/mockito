package org.mockito.internal.util.eventbus;

import static org.mockito.internal.util.Checks.checkNotNull;
import static org.mockito.internal.util.eventbus.compability.ListenerSupport.legacyListenerOf;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.mockito.listeners.Subscribe;

public class EventBus {

    private final Set<Subscriber> eventHandlers = new HashSet<Subscriber>();

    public void register(Object listener) {
        checkNotNull(listener, "The listener must not be null!");

        eventHandlers.addAll(eventHandlerOf(listener));
        eventHandlers.addAll( legacyListenerOf(listener));
    }

    

    public void post(Object event) {
        checkNotNull(event, "The event must not be null!");

        for (Subscriber eventHandler : eventHandlers) {
            eventHandler.tryHandleEvent(event);
        }
    }
    
    public void unregister(Object listener) {
        Collection<Subscriber> toBeRemoved= new LinkedList<Subscriber>();
        for (Subscriber subscriber : eventHandlers) {
            if (subscriber.getListener()==listener){
                toBeRemoved.add(subscriber);
            }
        }
        
        eventHandlers.removeAll(toBeRemoved);
    }

    private static Set<Subscriber> eventHandlerOf(Object listener) {

        Set<Subscriber> result = new HashSet<Subscriber>();
        for (Method method : listener.getClass().getMethods()) {

            if (isEventHandlerMethod(method)) {
                result.add(new Subscriber(listener, method));
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