/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import static org.mockito.internal.exceptions.Reporter.noArgumentValueWasCaptured;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unchecked")
public class CapturingMatcher<T> implements ArgumentMatcher<T>, CapturesArguments, VarargMatcher, Serializable {

    private final List<Object> arguments = new ArrayList<Object>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public boolean matches(Object argument) {
        return true;
    }

    public String toString() {
        return "<Capturing argument>";
    }

    public T getLastValue() {
        readLock.lock();
        try {
            if (arguments.isEmpty()) {
                throw noArgumentValueWasCaptured();
            }

            return (T) arguments.get(arguments.size() - 1);
        } finally {
            readLock.unlock();
        }
    }

    public List<T> getAllValues() {
        readLock.lock();
        try {
            return new ArrayList<T>((List) arguments);
        } finally {
            readLock.unlock();
        }
    }

    public void captureFrom(Object argument) {
        writeLock.lock();
        try {
            this.arguments.add(argument);
        } finally {
            writeLock.unlock();
        }
    }
}
