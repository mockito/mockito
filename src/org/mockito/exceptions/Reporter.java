/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import static org.mockito.exceptions.StringJoiner.*;

import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.cause.TooLittleInvocations;
import org.mockito.exceptions.cause.UndesiredInvocation;
import org.mockito.exceptions.cause.WantedDiffersFromActual;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.mockito.exceptions.verification.InvocationDiffersFromActual;
import org.mockito.exceptions.verification.WantedButNotInvoked;

/**
 * Reports verification and misusing errors.
 * <p>
 * One of the key points of mocking library is proper verification/exception
 * messages. All messages in one place makes it easier to tune and amend them.
 * <p>
 * Reporter can be injected and therefore is easily testable.
 * <p>
 * Generally, exception messages are full of line breaks to make them easy to
 * read (xunit plugins take only fraction of screen on modern IDEs).
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

        throw new InvocationDiffersFromActual(join(
                "Invocation differs from actual",
                "Wanted invocation:",
                wanted
            ), cause);
    }

    public void wantedButNotInvoked(String wanted) {
        throw new WantedButNotInvoked(join(
                    "Wanted but not invoked:",
                    wanted
        ));
    }

    public void tooManyActualInvocations(int wantedCount, int actualCount, String wanted, HasStackTrace firstUndesired) {
        UndesiredInvocation cause = new UndesiredInvocation(join("Undesired invocation:"));
        cause.setStackTrace(firstUndesired.getStackTrace());

        throw new TooManyActualInvocations(join(
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

        throw new TooLittleActualInvocations(join(
                wanted,
                "Wanted " + pluralize(wantedCount) + " but was " + actualCount
        ), cause);
    }

    public void noMoreInteractionsWanted(String undesired, HasStackTrace actualInvocationStackTrace) {
        UndesiredInvocation cause = new UndesiredInvocation(join(
                "Undesired invocation:", 
                undesired
        ));
        
        cause.setStackTrace(actualInvocationStackTrace.getStackTrace());
        throw new NoInteractionsWanted(join("No interactions wanted"), cause);
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