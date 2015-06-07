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

    //TODO why we need this method? The other thenReturn covers it.
    public OngoingStubbing<T> thenReturn(final T value) {
        return thenAnswer(new Returns(value));
    }

    public OngoingStubbing<T> thenReturn(final T value, final T... values) {
        OngoingStubbing<T> stubbing = thenReturn(value);            
        if (values == null) {
            //TODO below does not seem right
            return stubbing.thenReturn(null);
        }
        for (final T v: values) {
            stubbing = stubbing.thenReturn(v);
        }
        return stubbing;
    }

    private OngoingStubbing<T> thenThrow(final Throwable throwable) {
        return thenAnswer(new ThrowsException(throwable));
    }

    public OngoingStubbing<T> thenThrow(final Throwable... throwables) {
        if (throwables == null) {
            thenThrow((Throwable) null);
        }
        OngoingStubbing<T> stubbing = null;
        for (final Throwable t: throwables) {
            if (stubbing == null) {
                stubbing = thenThrow(t);                    
            } else {
                stubbing = stubbing.thenThrow(t);
            }
        }
        return stubbing;
    }        

    private OngoingStubbing<T> thenThrow(final Class<? extends Throwable> throwableClass) {
        return thenAnswer(new ThrowsExceptionClass(throwableClass));
    }

    @SuppressWarnings("unchecked")
    public OngoingStubbing<T> thenThrow(final Class<? extends Throwable>... throwableClasses) {
        if (throwableClasses == null) {
            thenThrow((Throwable) null);
        }
        OngoingStubbing<T> stubbing = null;
        for (final Class<? extends Throwable> t: throwableClasses) {
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

    public DeprecatedOngoingStubbing<T> toReturn(final T value) {
        return toAnswer(new Returns(value));
    }

    public DeprecatedOngoingStubbing<T> toThrow(final Throwable throwable) {
        return toAnswer(new ThrowsException(throwable));
    }
}
