/**
 * 
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.progress.DeprecatedOngoingStubbing;
import org.mockito.internal.progress.NewOngoingStubbing;
import org.mockito.internal.stubbing.answers.CallsRealMethod;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;

public abstract class BaseStubbing<T> implements NewOngoingStubbing<T>, DeprecatedOngoingStubbing<T> {
    public NewOngoingStubbing<T> thenReturn(T value) {
        return thenAnswer(new Returns(value));
    }

    public NewOngoingStubbing<T> thenReturn(T value, T... values) {
        NewOngoingStubbing<T> stubbing = thenReturn(value);            
        if (values == null) {
            return stubbing.thenReturn(null);
        }
        for (T v: values) {
            stubbing = stubbing.thenReturn(v);
        }
        return stubbing;
    }

    private NewOngoingStubbing<T> thenThrow(Throwable throwable) {
        return thenAnswer(new ThrowsException(throwable));
    }

    public NewOngoingStubbing<T> thenThrow(Throwable... throwables) {
        if (throwables == null) {
            thenThrow((Throwable) null);
        }
        NewOngoingStubbing<T> stubbing = null;
        for (Throwable t: throwables) {
            if (stubbing == null) {
                stubbing = thenThrow(t);                    
            } else {
                stubbing = stubbing.thenThrow(t);
            }
        }
        return stubbing;
    }        

    public NewOngoingStubbing<T> thenCallRealMethod() {
        return thenAnswer(new CallsRealMethod());
    }

    public DeprecatedOngoingStubbing<T> toReturn(T value) {
        return toAnswer(new Returns(value));
    }

    public DeprecatedOngoingStubbing<T> toThrow(Throwable throwable) {
        return toAnswer(new ThrowsException(throwable));
    }
}