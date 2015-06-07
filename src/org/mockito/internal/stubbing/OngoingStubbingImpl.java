/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;

public class OngoingStubbingImpl<T> extends BaseStubbing<T> {
    
    private final InvocationContainerImpl invocationContainerImpl;

    public OngoingStubbingImpl(final InvocationContainerImpl invocationContainerImpl) {
        this.invocationContainerImpl = invocationContainerImpl;
    }

    public OngoingStubbing<T> thenAnswer(final Answer<?> answer) {
        if(!invocationContainerImpl.hasInvocationForPotentialStubbing()) {
            new Reporter().incorrectUseOfApi();
        }

        invocationContainerImpl.addAnswer(answer);
        return new ConsecutiveStubbing<T>(invocationContainerImpl);
    }

    public OngoingStubbing<T> then(final Answer<?> answer) {
        return thenAnswer(answer);
    }

    public DeprecatedOngoingStubbing<T> toAnswer(final Answer<?> answer) {
        invocationContainerImpl.addAnswer(answer);
        return new ConsecutiveStubbing<T>(invocationContainerImpl);
    }

    public List<Invocation> getRegisteredInvocations() {
        //TODO interface for tests
        return invocationContainerImpl.getInvocations();
    }

    @SuppressWarnings("unchecked")
    public <M> M getMock() {
        return (M) invocationContainerImpl.invokedMock();
    }
}
