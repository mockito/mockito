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

    public ConsecutiveStubbing(InvocationContainerImpl invocationContainerImpl) {
        this.invocationContainerImpl = invocationContainerImpl;
    }

    public OngoingStubbing<T> thenAnswer(Answer<?> answer) {
        invocationContainerImpl.addConsecutiveAnswer(answer);
        return this;
    }
    
    public DeprecatedOngoingStubbing<T> toAnswer(Answer<?> answer) {
        invocationContainerImpl.addConsecutiveAnswer(answer);
        return this;
    }
}