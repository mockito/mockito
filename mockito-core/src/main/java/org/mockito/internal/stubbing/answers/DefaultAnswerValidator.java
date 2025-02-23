/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.mockito.internal.exceptions.Reporter.wrongTypeReturnedByDefaultAnswer;

import org.mockito.invocation.InvocationOnMock;

public abstract class DefaultAnswerValidator {
    public static void validateReturnValueFor(InvocationOnMock invocation, Object returnedValue)
            throws Throwable {
        InvocationInfo invocationInfo = new InvocationInfo(invocation);
        if (returnedValue != null && !invocationInfo.isValidReturnType(returnedValue.getClass())) {
            throw wrongTypeReturnedByDefaultAnswer(
                    invocation.getMock(),
                    invocationInfo.printMethodReturnType(),
                    returnedValue.getClass().getSimpleName(),
                    invocationInfo.getMethodName());
        }
    }
}
