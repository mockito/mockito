/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.mockito.internal.exceptions.Reporter.cannotStubVoidMethodWithAReturnValue;
import static org.mockito.internal.exceptions.Reporter.wrongTypeOfReturnValue;

import java.io.Serializable;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

public class Returns implements Answer<Object>, ValidableAnswer, Serializable {

    private static final long serialVersionUID = -6245608253574215396L;
    private final Object value;

    public Returns(Object value) {
        this.value = value;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        return value;
    }

    @Override
    public void validateFor(InvocationOnMock invocation) {
        InvocationInfo invocationInfo = new InvocationInfo(invocation);
        if (invocationInfo.isVoid()) {
            throw cannotStubVoidMethodWithAReturnValue(invocationInfo.getMethodName());
        }

        if (returnsNull() && invocationInfo.returnsPrimitive()) {
            throw wrongTypeOfReturnValue(
                    invocationInfo.printMethodReturnType(), "null", invocationInfo.getMethodName());
        }

        if (!returnsNull() && !invocationInfo.isValidReturnType(returnType())) {
            throw wrongTypeOfReturnValue(
                    invocationInfo.printMethodReturnType(),
                    printReturnType(),
                    invocationInfo.getMethodName());
        }
    }

    private String printReturnType() {
        return value.getClass().getSimpleName();
    }

    private Class<?> returnType() {
        return value.getClass();
    }

    private boolean returnsNull() {
        return value == null;
    }

    @Override
    public String toString() {
        return "Returns: " + value;
    }
}
