/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.mockito.internal.*;

public class MockInvocationHandler implements InvocationHandler, MockAwareInvocationHandler {

    protected final MocksControl control;

    public MockInvocationHandler(MocksControl control) {
        this.control = control;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        try {
            if (control.getState() instanceof RecordState) {
                LastControl.reportLastControl(control);
            }
            return control.getState().invoke(
                    new Invocation(proxy, method, args));
        } catch (RuntimeExceptionWrapper e) {
            throw e.getRuntimeException().fillInStackTrace();
        } catch (AssertionErrorWrapper e) {
            throw e.getAssertionError().fillInStackTrace();
        } catch (ThrowableWrapper t) {
            throw t.getThrowable().fillInStackTrace();
        }
    }

    public MocksControl getControl() {
        return control;
    }

    public void setMock(Object mock) {
    }
}