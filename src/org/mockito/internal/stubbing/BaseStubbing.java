/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.ThrowsExceptionClass;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;

public abstract class BaseStubbing<T> implements OngoingStubbing<T>, DeprecatedOngoingStubbing<T> {
    public OngoingStubbing<T> thenReturn(T... values) {
        if (values == null || values.length == 0) {
            return thenAnswer(new Returns(null));
        }
        OngoingStubbing<T> stubbing = null;
        for (T v: values) {
            if(stubbing == null) stubbing = thenAnswer(new Returns(v));
            else stubbing.thenReturn(v);
        }
        return stubbing;
    }

    private OngoingStubbing<T> thenThrow(Throwable throwable) {
        return thenAnswer(new ThrowsException(throwable));
    }

    public OngoingStubbing<T> thenThrow(Throwable... throwables) {
        if (throwables == null) {
            thenThrow((Throwable) null);
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

    private OngoingStubbing<T> thenThrow(Class<? extends Throwable> throwableClass) {
        return thenAnswer(new ThrowsExceptionClass(throwableClass));
    }

    public OngoingStubbing<T> thenThrow(Class<? extends Throwable>... throwableClasses) {
        if (throwableClasses == null) {
            thenThrow((Throwable) null);
        }
        OngoingStubbing<T> stubbing = null;
        for (Class<? extends Throwable> t: throwableClasses) {
            if (stubbing == null) {
                stubbing = thenThrow(t);
            } else {
                stubbing = stubbing.thenThrow(t);
            }
        }
        return stubbing;
    }

    public OngoingStubbing<T> thenCallRealMethod() {
        return thenAnswer(new CallsRealMethods());
    }

    public DeprecatedOngoingStubbing<T> toReturn(T value) {
        return toAnswer(new Returns(value));
    }

    public DeprecatedOngoingStubbing<T> toThrow(Throwable throwable) {
        return toAnswer(new ThrowsException(throwable));
    }
}
