/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubbing;

import java.util.List;

//TODO this class needs to be exposed to complete cleaning of the API
public interface InvocationContainer {

    /**
     * Due to the nature of the syntax, every invocation on a mock object can be a stubbing or a regular mock invocation.
     * Mockito calls this method for every invocation and for every invocation in when().then() style of stubbing.
     */
    void setInvocationForPotentialStubbing(MatchableInvocation invocation);

    /**
     * Needed for partial mocking / spying scenarios.
     * <p>
     * Mockito uses it to redo setting invocation for potential stubbing in case of partial mocks / spies.
     * Without it, the real method inside 'when' might have delegated to other self method
     * and overwrite the intended stubbed method with a different one.
     * This means we would be stubbing a wrong method.
     * Typically this would led to runtime exception that validates return type with stubbed method signature.
     */
    void resetInvocationForPotentialStubbing(MatchableInvocation invocation);

    StubbedInvocationMatcher findAnswerFor(Invocation invocation);

    /**
     * Set collection of answers declared by the user
     * using doReturn/doAnswer/doThrow/doNothing style of stubbing.
     * See {@link org.mockito.Mockito#doReturn(Object)}.
     *
     * @param answers recorded by user with doReturn/doAnswer/doNothing/doThrow stubbing style
     */
    void setAnswersForStubbing(List<Answer<?>> answers);

    /**
     * Informs Mockito that there were Answers recorded with
     * doReturn() style of stubbing.
     * TODO merge with setMethodForStubbing
     */
    boolean hasAnswersForStubbing();

    /**
     * Checks if there is at least one registered invocation.
     * TODO unexpose, it is used in one place and we can push it down the stack
     */
    boolean hasInvocationForPotentialStubbing();

    /**
     * Sets the method that is being stubbed with doReturn() style of stubbing.
     */
    void setMethodForStubbing(MatchableInvocation invocation);

    List<Invocation> getInvocations();

    void clearInvocations();

    List<Stubbing> getStubbedInvocations();
}
