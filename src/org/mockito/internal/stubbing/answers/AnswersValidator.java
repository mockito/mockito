/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class AnswersValidator {

    private Reporter reporter = new Reporter();
    
    public void validate(Answer<?> answer, Invocation invocation) {
        if (answer instanceof ThrowsException) {
            validateException((ThrowsException) answer, invocation);
        }
        
        if (answer instanceof Returns) {
            validateReturnValue((Returns) answer, invocation);
        }
        
        if (answer instanceof DoesNothing) {
            validateDoNothing((DoesNothing) answer, invocation);
        }
        
        if (answer instanceof CallsRealMethods) {
            validateMockingConcreteClass((CallsRealMethods) answer, invocation);
        }
    }

    private void validateMockingConcreteClass(CallsRealMethods answer, Invocation invocation) {
        if (invocation.isDeclaredOnInterface()) {
            reporter.cannotCallRealMethodOnInterface();
        }
    }

    private void validateDoNothing(DoesNothing answer, Invocation invocation) {
        if (!invocation.isVoid()) {
            reporter.onlyVoidMethodsCanBeSetToDoNothing();
        }
    }

    private void validateReturnValue(Returns answer, Invocation invocation) {
        if (invocation.isVoid()) {
            reporter.cannotStubVoidMethodWithAReturnValue(invocation.getMethod().getName());
        }
        
        if (answer.returnsNull() && invocation.returnsPrimitive()) {
            reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), "null", invocation.getMethodName());
        } 

        if (!answer.returnsNull() && !invocation.isValidReturnType(answer.getReturnType())) {
            reporter.wrongTypeOfReturnValue(invocation.printMethodReturnType(), answer.printReturnType(), invocation.getMethodName());
        }
    }

    private void validateException(ThrowsException answer, Invocation invocation) {
        Throwable throwable = answer.getThrowable();
        if (throwable == null) {
            reporter.cannotStubWithNullThrowable();
        }
        
        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }
        
        if (!invocation.isValidException(throwable)) {
            reporter.checkedExceptionInvalid(throwable);
        }
    }
}