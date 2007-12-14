/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import static org.mockito.exceptions.StringJoiner.join;

import org.mockito.exceptions.cause.*;
import org.mockito.exceptions.misusing.*;
import org.mockito.exceptions.parents.*;
import org.mockito.exceptions.verification.*;

/**
 * One of the key points of mocking library is proper verification/exception
 * messages. All messages in one place makes it easier to tune and amend.
 */
public class Reporter {
    
    private String pluralize(int number) {
        return number == 1 ? "1 time" : number + " times";
    }

    public void mocksHaveToBePassedAsArguments() {
        throw new MockitoException(join(
                "Method requires arguments.",
                "Pass mocks that should be verified, e.g:",
                "verifyNoMoreInteractions(mockOne, mockTwo)"
                ));
    }

    public void strictlyRequiresFamiliarMock() {
        throw new MockitoException(join(
                "Strictly can only verify mocks that were passed in during creation of Strictly. E.g:",
                "strictly = createStrictOrderVerifier(mockOne)",
                "strictly.verify(mockOne).doStuff()"
                ));
    }

    public void mocksHaveToBePassedWhenCreatingStrictly() {
        throw new MockitoException(join(
                "Method requires arguments.",
                "Pass mocks that require strict order verification, e.g:",
                "createStrictOrderVerifier(mockOne, mockTwo)"
                ));
    }

    public void checkedExceptionInvalid(Throwable t) {
        throw new MockitoException(join(
        		"Checked exception is invalid for this method",
        		"Invalid: " + t
        		));
    }

    public void cannotStubWithNullThrowable() {
        throw new MockitoException(join(
                "Cannot stub with null throwable"
                ));
        
    }
    
    public void wantedInvocationDiffersFromActual(String wanted, String actual, HasStackTrace actualInvocationStackTrace) {
        WantedDiffersFromActual cause = new WantedDiffersFromActual(join(
                "Actual invocation:",
                actual
            ));
            
        cause.setStackTrace(actualInvocationStackTrace.getStackTrace());
        
        throw new VerificationError(join(
                "Invocation differs from actual",
                "Wanted invocation:",
                wanted
            ), cause);
    }
    
    public void wantedButNotInvoked(String wanted) {
        throw new VerificationError(join(
                    "Wanted but not invoked:",
                    wanted        
        ));
    }
    
    public void numberOfInvocationsDiffers(int wantedCount, int actualCount, String wanted) {
        throw new NumberOfInvocationsError(join(
                wanted,
                "Wanted " + pluralize(wantedCount) + " but was " + actualCount
        ));
    }

    public void tooManyActualInvocations(int wantedCount, int actualCount, String wanted, HasStackTrace firstUndesired) {
        FirstUndesiredInvocation cause = new FirstUndesiredInvocation(join("First undesired invocation:"));
        cause.setStackTrace(firstUndesired.getStackTrace());
        
        throw new TooManyActualInvocationsError(join(
                wanted,
                "Wanted " + pluralize(wantedCount) + " but was " + actualCount
        ), cause);
    }
    
    public void tooLittleActualInvocations(int wantedCount, int actualCount, String wanted, HasStackTrace lastActualInvocationStackTrace) {
        TooLittleInvocations cause = null;
        if (lastActualInvocationStackTrace != null) {
            cause = new TooLittleInvocations(join("Too little invocations:"));
            cause.setStackTrace(lastActualInvocationStackTrace.getStackTrace());
        }
        
        throw new TooLittleActualInvocationsError(join(
                wanted,
                "Wanted " + pluralize(wantedCount) + " but was " + actualCount
        ), cause);  
    }

    public void noMoreInteractionsWanted(String undesired, HasStackTrace actualInvocationStackTrace) {
        UndesiredInvocation cause = buildUndesiredInvocationCause(actualInvocationStackTrace, "Undesired invocation:", undesired);
        throw new VerificationError(join("No more interactions wanted"), cause);
    }
    
    public void zeroInteractionsWanted(String undesired, HasStackTrace actualInvocationStackTrace) {
        UndesiredInvocation cause = buildUndesiredInvocationCause(actualInvocationStackTrace, "Undesired invocation:", undesired);
        throw new VerificationError(join("Zero interactions wanted"), cause);
    }

    private UndesiredInvocation buildUndesiredInvocationCause(HasStackTrace actualInvocationStackTrace, String ... messageLines) {
        UndesiredInvocation cause = new UndesiredInvocation(join(messageLines));
        cause.setStackTrace(actualInvocationStackTrace.getStackTrace());
        return cause;
    }

    public void unfinishedStubbing() {
        throw new UnfinishedStubbingException(join(
                "Unifinished stubbing detected, e.g. toReturn() is missing",
                "Examples of proper stubbing:",
                "stub(mock.isOk()).andReturn(true);",
                "stub(mock.isOk()).andThrows(exception);",
                "stubVoid(mock).toThrow(exception).on().someMethod();"
        ));
    }

    public void missingMethodInvocation() {
        throw new MissingMethodInvocationException(join(
                "stub() requires an argument which has to be a proper method call on a mock object"
        ));
    }

    public void unfinishedVerificationException() {
        throw new UnfinishedVerificationException(join(
                "Previous verify(mock) doesn't have a method call.",
                "Should be something like that: verify(mock).doSomething()"
        ));
    }
}