/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.invocation.Invocation;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.List;

import static org.mockito.internal.exceptions.Reporter.incorrectUseOfApi;

public class OngoingStubbingImpl<T> extends BaseStubbing<T> {

    private final InvocationContainerImpl invocationContainer;
    private Strictness strictness;

    public OngoingStubbingImpl(InvocationContainerImpl invocationContainer) {
        super(invocationContainer.invokedMock());
        this.invocationContainer = invocationContainer;
    }

    @Override
    public OngoingStubbing<T> thenAnswer(Answer<?> answer) {
        if(!invocationContainer.hasInvocationForPotentialStubbing()) {
            throw incorrectUseOfApi();
        }

        invocationContainer.addAnswer(answer, strictness);
        return new ConsecutiveStubbing<T>(invocationContainer);
    }

    public List<Invocation> getRegisteredInvocations() {
        //TODO interface for tests
        return invocationContainer.getInvocations();
    }

    public void setStrictness(Strictness strictness) {
        this.strictness = strictness;
    }
}


