/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.progress.DeprecatedOngoingStubbing;
import org.mockito.internal.progress.NewOngoingStubbing;
import org.mockito.internal.verification.RegisteredInvocations;
import org.mockito.stubbing.Answer;

public class OngoingStubbingImpl<T> extends BaseStubbing<T> {
    
    private final MockitoStubber mockitoStubber;
    private final RegisteredInvocations registeredInvocations;

    public OngoingStubbingImpl(MockitoStubber mockitoStubber,
            RegisteredInvocations registeredInvocations) {
        this.mockitoStubber = mockitoStubber;
        this.registeredInvocations = registeredInvocations;
    }

    public NewOngoingStubbing<T> thenAnswer(Answer<?> answer) {
        registeredInvocations.removeLast();
        mockitoStubber.addAnswer(answer);
        return new ConsecutiveStubbing<T>(mockitoStubber);
    }

    public DeprecatedOngoingStubbing<T> toAnswer(Answer<?> answer) {
        registeredInvocations.removeLast();
        mockitoStubber.addAnswer(answer);
        return new ConsecutiveStubbing<T>(mockitoStubber);
    }

    public RegisteredInvocations getRegisteredInvocations() {
        return registeredInvocations;
    }
}