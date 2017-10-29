/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.invocation.InvocationOnMock;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * Interface that wraps a 'real' method of the mock object.
 * Needed for test spies or {@link InvocationOnMock#callRealMethod()}.
 */
public interface RealMethod extends Serializable {

    enum IsIllegal implements RealMethod {

        INSTANCE;

        @Override
        public boolean isInvokable() {
            return false;
        }

        @Override
        public Object invoke() {
            throw new IllegalStateException();
        }
    }

    class FromCallable implements RealMethod {

        private static final long serialVersionUID = 47957363950483625L;

        private final Callable<?> callable;

        public FromCallable(Callable<?> callable) {
            this.callable = callable;
        }

        @Override
        public boolean isInvokable() {
            return true;
        }

        @Override
        public Object invoke() throws Throwable {
            try {
                return callable.call();
            } catch (Throwable t) {
                new ConditionalStackTraceFilter().filter(t);
                throw t;
            }
        }
    }

    boolean isInvokable();

    Object invoke() throws Throwable;
}
