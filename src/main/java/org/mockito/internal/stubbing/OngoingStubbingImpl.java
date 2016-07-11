/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.internal.exceptions.Reporter.incorrectUseOfApi;

import java.util.List;

public class OngoingStubbingImpl<T> extends BaseStubbing<T> {
    
    private final InvocationContainerImpl invocationContainerImpl;

    public OngoingStubbingImpl(InvocationContainerImpl invocationContainerImpl) {
        this.invocationContainerImpl = invocationContainerImpl;
    }

    public OngoingStubbing<T> thenAnswer(Answer<?> answer) {
        if(!invocationContainerImpl.hasInvocationForPotentialStubbing()) {
            throw incorrectUseOfApi();
        }

        invocationContainerImpl.addAnswer(answer);
        return new ConsecutiveStubbing<T>(invocationContainerImpl);
    }

    public OngoingStubbing<T> then(Answer<?> answer) {
        return thenAnswer(answer);
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
