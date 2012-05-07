/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;

import java.util.List;

public class OngoingStubbingImpl<T> extends BaseStubbing<T> {
    
    private final InvocationContainerImpl invocationContainerImpl;

    public OngoingStubbingImpl(InvocationContainerImpl invocationContainerImpl) {
        this.invocationContainerImpl = invocationContainerImpl;
    }

    public OngoingStubbing<T> thenAnswer(Answer<?> answer) {
        if(!invocationContainerImpl.hasInvocationForPotentialStubbing()) {
            new Reporter().incorrectUseOfApi();
        }

        invocationContainerImpl.addAnswer(answer);
        return new ConsecutiveStubbing<T>(invocationContainerImpl);
    }

    public OngoingStubbing<T> then(Answer<?> answer) {
        return thenAnswer(answer);
    }

    public DeprecatedOngoingStubbing<T> toAnswer(Answer<?> answer) {
        invocationContainerImpl.addAnswer(answer);
        return new ConsecutiveStubbing<T>(invocationContainerImpl);
    }

    public List<Invocation> getRegisteredInvocations() {
        //TODO interface for tests
        return invocationContainerImpl.getInvocations();
    }

    public <M> M getMock() {
        return (M) invocationContainerImpl.invokedMock();
    }
}
