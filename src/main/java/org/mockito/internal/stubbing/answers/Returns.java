/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

import static org.mockito.internal.exceptions.Reporter.cannotStubVoidMethodWithAReturnValue;
import static org.mockito.internal.exceptions.Reporter.wrongTypeOfReturnValue;

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
        MethodInfo methodInfo = new MethodInfo(invocation);
        if (methodInfo.isVoid()) {
            throw cannotStubVoidMethodWithAReturnValue(methodInfo.getMethodName());
        }

        if (returnsNull() && methodInfo.returnsPrimitive()) {
            throw wrongTypeOfReturnValue(methodInfo.printMethodReturnType(), "null", methodInfo.getMethodName());
        }

        if (!returnsNull() && !methodInfo.isValidReturnType(returnType())) {
            throw wrongTypeOfReturnValue(methodInfo.printMethodReturnType(), printReturnType(), methodInfo.getMethodName());
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
}
