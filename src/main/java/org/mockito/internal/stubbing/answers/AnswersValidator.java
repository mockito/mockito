/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

import static org.mockito.internal.exceptions.Reporter.wrongTypeReturnedByDefaultAnswer;

@Deprecated
public class AnswersValidator {


    public void validate(Answer<?> answer, InvocationOnMock invocation) {
        if (answer instanceof ValidableAnswer) {
            ((ValidableAnswer) answer).validateFor(invocation);
        }
    }

    public void validateDefaultAnswerReturnedValue(Invocation invocation, Object returnedValue) {
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
