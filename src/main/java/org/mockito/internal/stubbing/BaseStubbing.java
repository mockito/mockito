/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.internal.exceptions.Reporter.notAnException;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static org.objenesis.ObjenesisHelper.newInstance;

public abstract class BaseStubbing<T> implements OngoingStubbing<T> {


    // Keep strong ref to mock preventing premature garbage collection when using 'One-liner stubs'. See #1541.
    private final Object strongMockRef;

    BaseStubbing(Object mock) {
        this.strongMockRef = mock;
    }

    @Override
    public OngoingStubbing<T> then(Answer<?> answer) {
        return thenAnswer(answer);
    }

    @Override
    public OngoingStubbing<T> thenReturn(T value) {
        return thenAnswer(new Returns(value));
    }

    @Override
    public OngoingStubbing<T> thenReturn(T value, T... values) {
        OngoingStubbing<T> stubbing = thenReturn(value);
        if (values == null) {
            // For no good reason we're configuring null answer here
            // This has been like that since forever, so let's keep it for compatibility (unless users complain)
            return stubbing.thenReturn(null);
        }
        for (T v : values) {
            stubbing = stubbing.thenReturn(v);
        }
        return stubbing;
    }

    private OngoingStubbing<T> thenThrow(Throwable throwable) {
        return thenAnswer(new ThrowsException(throwable));
    }

    @Override
    public OngoingStubbing<T> thenThrow(Throwable... throwables) {
        if (throwables == null) {
            return thenThrow((Throwable) null);
        }
        OngoingStubbing<T> stubbing = null;
        for (Throwable t : throwables) {
            if (stubbing == null) {
                stubbing = thenThrow(t);
            } else {
                stubbing = stubbing.thenThrow(t);
            }
        }
        return stubbing;
    }

    @Override
    public OngoingStubbing<T> thenThrow(Class<? extends Throwable> throwableType) {
        if (throwableType == null) {
            mockingProgress().reset();
            throw notAnException();
        }
        return thenThrow(newInstance(throwableType));
    }

    @Override
    public OngoingStubbing<T> thenThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        if (nextToBeThrown == null) {
            return thenThrow((Class<Throwable>) null);
        }
        OngoingStubbing<T> stubbing = thenThrow(toBeThrown);
        for (Class<? extends Throwable> t : nextToBeThrown) {
            stubbing = stubbing.thenThrow(t);
        }
        return stubbing;
    }

    @Override
    public OngoingStubbing<T> thenCallRealMethod() {
        return thenAnswer(new CallsRealMethods());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M> M getMock() {
        return (M) this.strongMockRef;
    }
}


