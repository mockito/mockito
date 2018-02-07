/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.invocation.InvocationFactory;
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

    class FromCallable extends FromBehavior implements RealMethod {
        public FromCallable(final Callable<?> callable) {
            super(new InvocationFactory.RealMethodBehavior() {
                @Override
                public Object call() throws Throwable {
                    return callable.call();
                }
            });
        }
    }

    class FromBehavior implements RealMethod {

        private final InvocationFactory.RealMethodBehavior<?> behavior;

        FromBehavior(InvocationFactory.RealMethodBehavior<?> behavior) {
            this.behavior = behavior;
        }

        @Override
        public boolean isInvokable() {
            return true;
        }

        @Override
        public Object invoke() throws Throwable {
            try {
                return behavior.call();
            } catch (Throwable t) {
                new ConditionalStackTraceFilter().filter(t);
                throw t;
            }
        }
    }

    boolean isInvokable();

    Object invoke() throws Throwable;
}
