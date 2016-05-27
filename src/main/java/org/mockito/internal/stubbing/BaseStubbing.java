/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.ThrowsExceptionClass;
import org.mockito.stubbing.OngoingStubbing;

public abstract class BaseStubbing<T> implements OngoingStubbing<T> {

    public OngoingStubbing<T> thenReturn(T value) {
        return thenAnswer(new Returns(value));
    }

    @SuppressWarnings({"unchecked","vararg"})
    public OngoingStubbing<T> thenReturn(T value, T... values) {
        OngoingStubbing<T> stubbing = thenReturn(value);
        if (values == null) {
            //TODO below does not seem right
            return stubbing.thenReturn(null);
        }
        for (T v: values) {
            stubbing = stubbing.thenReturn(v);
        }
        return stubbing;
    }

    private OngoingStubbing<T> thenThrow(Throwable throwable) {
        return thenAnswer(new ThrowsException(throwable));
    }

    public OngoingStubbing<T> thenThrow(Throwable... throwables) {
        if (throwables == null) {
            return thenThrow((Throwable) null);
        }
        OngoingStubbing<T> stubbing = null;
        for (Throwable t: throwables) {
            if (stubbing == null) {
                stubbing = thenThrow(t);                    
            } else {
                stubbing = stubbing.thenThrow(t);
            }
        }
        return stubbing;
    }        

    public OngoingStubbing<T> thenThrow(Class<? extends Throwable> throwableType) {
        return thenAnswer(new ThrowsExceptionClass(throwableType));
    }

    @SuppressWarnings ({"unchecked", "varargs"})
    public OngoingStubbing<T> thenThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        if (nextToBeThrown == null) {
            thenThrow((Throwable) null);
        }
        OngoingStubbing<T> stubbing = thenThrow(toBeThrown);
        for (Class<? extends Throwable> t: nextToBeThrown) {
            stubbing = stubbing.thenThrow(t);
        }
        return stubbing;
    }

    public OngoingStubbing<T> thenCallRealMethod() {
        return thenAnswer(new CallsRealMethods());
    }
}