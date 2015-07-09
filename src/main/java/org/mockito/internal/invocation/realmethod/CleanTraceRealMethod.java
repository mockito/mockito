/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.realmethod;

import org.mockito.internal.creation.util.MockitoMethodProxy;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;

import java.io.Serializable;

/**
 * Provides stack trace filtering on exception.
 */
public class CleanTraceRealMethod implements RealMethod, Serializable {

    private static final long serialVersionUID = 3596550785818938496L;
    private final RealMethod realMethod;

    public CleanTraceRealMethod(MockitoMethodProxy methodProxy) {
        this(new DefaultRealMethod(methodProxy));
    }

    public CleanTraceRealMethod(RealMethod realMethod) {
        this.realMethod = realMethod;
    }

    public Object invoke(Object target, Object[] arguments) throws Throwable {
        try {
            return realMethod.invoke(target, arguments);
        } catch (Throwable t) {
            new ConditionalStackTraceFilter().filter(t);
            throw t;
        }
    }
}