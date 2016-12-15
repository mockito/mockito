/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;

import static org.mockito.internal.exceptions.Reporter.wrongTypeReturnedByDefaultAnswer;

public abstract class DefaultAnswerValidator {
    public static void validateReturnValueFor(InvocationOnMock invocation, Object returnedValue) throws Throwable {
        MethodInfo methodInfo = new MethodInfo(invocation);
        if (returnedValue != null && !methodInfo.isValidReturnType(returnedValue.getClass())) {
            throw wrongTypeReturnedByDefaultAnswer(
                    invocation.getMock(),
                    methodInfo.printMethodReturnType(),
                    returnedValue.getClass().getSimpleName(),
                    methodInfo.getMethodName());
        }
    }
}
