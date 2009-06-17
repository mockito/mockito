/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.verification.RegisteredInvocations;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;

public class OngoingStubbingImpl<T> extends BaseStubbing<T> {
    
    private final MockitoStubber mockitoStubber;
    private final RegisteredInvocations registeredInvocations;

    public OngoingStubbingImpl(MockitoStubber mockitoStubber,
            RegisteredInvocations registeredInvocations) {
        this.mockitoStubber = mockitoStubber;
        this.registeredInvocations = registeredInvocations;
    }

    public OngoingStubbing<T> thenAnswer(Answer<?> answer) {
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