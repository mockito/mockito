/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.answers.ReturnsIdentity;

public class AnswersValidator {

    private Reporter reporter = new Reporter();
    
    public void validate(Answer<?> answer, Invocation invocation) {
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

        if (answer instanceof ReturnsIdentity) {
            ReturnsIdentity returnsIdentity = (ReturnsIdentity) answer;
            validateReturnArgIdentity(returnsIdentity, invocation);
        }
    }

    private void validateReturnArgIdentity(ReturnsIdentity returnsIdentity, Invocation invocation) {
        returnsIdentity.validateIndexWithinInvocationRange(invocation);

        MethodInfo methodInfo = new MethodInfo(invocation);
        if (!methodInfo.isValidReturnType(returnsIdentity.returnedTypeOnSignature(invocation))) {
            new Reporter().wrongTypeOfArgumentToReturn(invocation, methodInfo.printMethodReturnType(),
                                                       returnsIdentity.returnedTypeOnSignature(invocation),
                                                       returnsIdentity.wantedArgumentIndex());
        }

    }

    private void validateMockingConcreteClass(CallsRealMethods answer, MethodInfo methodInfo) {
        if (methodInfo.isDeclaredOnInterface()) {
            reporter.cannotCallRealMethodOnInterface();
        }
    }

    private void validateDoNothing(DoesNothing answer, MethodInfo methodInfo) {
        if (!methodInfo.isVoid()) {
            reporter.onlyVoidMethodsCanBeSetToDoNothing();
        }
    }

    private void validateReturnValue(Returns answer, MethodInfo methodInfo) {
        if (methodInfo.isVoid()) {
            reporter.cannotStubVoidMethodWithAReturnValue(methodInfo.getMethodName());
        }
        
        if (answer.returnsNull() && methodInfo.returnsPrimitive()) {
            reporter.wrongTypeOfReturnValue(methodInfo.printMethodReturnType(), "null", methodInfo.getMethodName());
        } 

        if (!answer.returnsNull() && !methodInfo.isValidReturnType(answer.getReturnType())) {
            reporter.wrongTypeOfReturnValue(methodInfo.printMethodReturnType(), answer.printReturnType(), methodInfo.getMethodName());
        }
    }

    private void validateException(ThrowsException answer, MethodInfo methodInfo) {
        Throwable throwable = answer.getThrowable();
        if (throwable == null) {
            reporter.cannotStubWithNullThrowable();
        }
        
        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }
        
        if (!methodInfo.isValidException(throwable)) {
            reporter.checkedExceptionInvalid(throwable);
        }
    }
}