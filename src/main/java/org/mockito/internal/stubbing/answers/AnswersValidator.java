/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

import static org.mockito.internal.exceptions.Reporter.cannotCallAbstractRealMethod;
import static org.mockito.internal.exceptions.Reporter.cannotStubVoidMethodWithAReturnValue;
import static org.mockito.internal.exceptions.Reporter.cannotStubWithNullThrowable;
import static org.mockito.internal.exceptions.Reporter.checkedExceptionInvalid;
import static org.mockito.internal.exceptions.Reporter.onlyVoidMethodsCanBeSetToDoNothing;
import static org.mockito.internal.exceptions.Reporter.wrongTypeOfReturnValue;
import static org.mockito.internal.exceptions.Reporter.wrongTypeReturnedByDefaultAnswer;

@Deprecated
public class AnswersValidator {


    public void validate(Answer<?> answer, InvocationOnMock invocation) {
        if (answer instanceof ValidableAnswer) {
            ((ValidableAnswer) answer).validateFor(invocation);
        }

        MethodInfo methodInfo = new MethodInfo(invocation);
        if (answer instanceof ThrowsException) {
            validateException((ThrowsException) answer, methodInfo);
        }

        if (answer instanceof Returns) {
            validateReturnValue((Returns) answer, methodInfo);
        }

        if (answer instanceof DoesNothing) {
            validateDoNothing((DoesNothing) answer, methodInfo);
        }

        if (answer instanceof CallsRealMethods) {
            validateMockingConcreteClass((CallsRealMethods) answer, methodInfo);
        }
    }

    private void validateMockingConcreteClass(CallsRealMethods answer, MethodInfo methodInfo) {
        if (methodInfo.isAbstract()) {
            throw cannotCallAbstractRealMethod();
        }
    }

    private void validateDoNothing(DoesNothing answer, MethodInfo methodInfo) {
        if (!methodInfo.isVoid()) {
            throw onlyVoidMethodsCanBeSetToDoNothing();
        }
    }

    private void validateReturnValue(Returns answer, MethodInfo methodInfo) {
        if (methodInfo.isVoid()) {
            throw cannotStubVoidMethodWithAReturnValue(methodInfo.getMethodName());
        }

        if (answer.returnsNull() && methodInfo.returnsPrimitive()) {
            throw wrongTypeOfReturnValue(methodInfo.printMethodReturnType(), "null", methodInfo.getMethodName());
        }

        if (!answer.returnsNull() && !methodInfo.isValidReturnType(answer.getReturnType())) {
            throw wrongTypeOfReturnValue(methodInfo.printMethodReturnType(), answer.printReturnType(), methodInfo.getMethodName());
        }
    }

    private void validateException(ThrowsException answer, MethodInfo methodInfo) {
        Throwable throwable = answer.getThrowable();
        if (throwable == null) {
            throw cannotStubWithNullThrowable();
        }

        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }

        if (!methodInfo.isValidException(throwable)) {
            throw checkedExceptionInvalid(throwable);
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
