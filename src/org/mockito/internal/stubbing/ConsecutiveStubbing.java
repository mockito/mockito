/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;

public class ConsecutiveStubbing<T> extends BaseStubbing<T> {
    private final InvocationContainerImpl invocationContainerImpl;

    public ConsecutiveStubbing(final InvocationContainerImpl invocationContainerImpl) {
        this.invocationContainerImpl = invocationContainerImpl;
    }

    public OngoingStubbing<T> thenAnswer(final Answer<?> answer) {
        invocationContainerImpl.addConsecutiveAnswer(answer);
        return this;
    }

    public OngoingStubbing<T> then(final Answer<?> answer) {
        return thenAnswer(answer);
    }
    
    public DeprecatedOngoingStubbing<T> toAnswer(final Answer<?> answer) {
        invocationContainerImpl.addConsecutiveAnswer(answer);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <M> M getMock() {
        return (M) invocationContainerImpl.invokedMock();
    }
}