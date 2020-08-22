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

    static {
        ClassLoader classLoader = MockMethodDispatcher.class.getClassLoader();
        if (classLoader != null) {
            // Do not use Mockito classes in here as this is executed on the boot loader.
            throw new IllegalStateException(
                    MockMethodDispatcher.class.getName()
                            + " is not loaded by the bootstrap class loader but by an instance of "
                            + classLoader.getClass().getName()
                            + ".\n\nThis causes the inline mock maker to not work as expected. "
                            + "Please contact the maintainer of this class loader implementation "
                            + "to assure that this class is never loaded by another class loader. "
                            + "The bootstrap class loader must always be queried first for this "
                            + "class for Mockito's inline mock maker to function correctly.");
        }
    }

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

    @SuppressWarnings("unused")
    public static boolean isConstructorMock(String identifier, Class<?> type) {
        return DISPATCHERS.get(identifier).isConstructorMock(type);
    }

    @SuppressWarnings("unused")
    public static Object handleConstruction(
            String identifier,
            Class<?> type,
            Object object,
            Object[] arguments,
            String[] parameterTypeNames) {
        return DISPATCHERS
                .get(identifier)
                .handleConstruction(type, object, arguments, parameterTypeNames);
    }

    public abstract Callable<?> handle(Object instance, Method origin, Object[] arguments)
            throws Throwable;

    public abstract Callable<?> handleStatic(Class<?> type, Method origin, Object[] arguments)
            throws Throwable;

    public abstract Object handleConstruction(
            Class<?> type, Object object, Object[] arguments, String[] parameterTypeNames);

    public abstract boolean isMock(Object instance);

    public abstract boolean isMocked(Object instance);

    public abstract boolean isMockedStatic(Class<?> type);

    public abstract boolean isOverridden(Object instance, Method origin);

    public abstract boolean isConstructorMock(Class<?> type);
}
