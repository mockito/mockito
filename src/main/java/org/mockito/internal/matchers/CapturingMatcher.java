/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.mockito.internal.exceptions.Reporter.noArgumentValueWasCaptured;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.util.Primitives;

public class CapturingMatcher<T> implements ArgumentMatcher<T>, CapturesArguments, Serializable {

    private final Class<? extends T> clazz;
    private final List<T> arguments = new ArrayList<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public CapturingMatcher(final Class<? extends T> clazz) {
        this.clazz = Objects.requireNonNull(clazz);
    }

    @Override
    public boolean matches(Object argument) {
        if (argument == null) {
            return true;
        }

        if (Primitives.isPrimitiveOrWrapper(clazz)) {
            return Primitives.isAssignableFromWrapper(clazz, argument.getClass());
        }

        return clazz.isAssignableFrom(argument.getClass());
    }

    @Override
    public String toString() {
        return "<Capturing argument: " + clazz.getSimpleName() + ">";
    }

    public T getLastValue() {
        readLock.lock();
        try {
            if (arguments.isEmpty()) {
                throw noArgumentValueWasCaptured();
            }

            return arguments.get(arguments.size() - 1);
        } finally {
            readLock.unlock();
        }
    }

    public List<T> getAllValues() {
        readLock.lock();
        try {
            return new ArrayList<>(arguments);
        } finally {
            readLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void captureFrom(Object argument) {
        writeLock.lock();
        try {
            this.arguments.add((T) argument);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Class<?> type() {
        return clazz;
    }
}
