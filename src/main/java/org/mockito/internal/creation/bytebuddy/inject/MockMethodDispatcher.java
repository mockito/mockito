/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy.inject;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class MockMethodDispatcher {

    private static final ConcurrentMap<String, MockMethodDispatcher> DISPATCHERS =
            new ConcurrentHashMap<>();

    public static MockMethodDispatcher get(String identifier, Object mock) {
        if (mock == DISPATCHERS) {
            // Avoid endless loop if ConcurrentHashMap was redefined to check for being a mock.
            return null;
        } else {
            return DISPATCHERS.get(identifier);
        }
    }

    public static MockMethodDispatcher getStatic(String identifier, Class<?> type) {
        if (MockMethodDispatcher.class.isAssignableFrom(type) || type == ConcurrentHashMap.class) {
            // Avoid endless loop for lookups of self.
            return null;
        } else {
            return DISPATCHERS.get(identifier);
        }
    }

    public static void set(String identifier, MockMethodDispatcher dispatcher) {
        DISPATCHERS.putIfAbsent(identifier, dispatcher);
    }

    public abstract Callable<?> handle(Object instance, Method origin, Object[] arguments)
            throws Throwable;

    public abstract Callable<?> handleStatic(Class<?> type, Method origin, Object[] arguments)
            throws Throwable;

    public abstract boolean isMock(Object instance);

    public abstract boolean isMocked(Object instance);

    public abstract boolean isMockedStatic(Class<?> type);

    public abstract boolean isOverridden(Object instance, Method origin);
}
