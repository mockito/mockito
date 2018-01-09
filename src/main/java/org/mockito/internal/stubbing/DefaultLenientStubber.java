/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.MockitoCore;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.LenientStubber;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;

public class DefaultLenientStubber implements LenientStubber {

    private final static MockitoCore MOCKITO_CORE = new MockitoCore();

    @Override
    public Stubber doThrow(Throwable... toBeThrown) {
        return null;
    }

    @Override
    public Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return null;
    }

    @Override
    public Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        return null;
    }

    @Override
    public Stubber doAnswer(Answer answer) {
        return null;
    }

    @Override
    public Stubber doNothing() {
        return null;
    }

    @Override
    public Stubber doReturn(Object toBeReturned) {
        return MOCKITO_CORE.stubber(Strictness.LENIENT).doReturn(toBeReturned);
    }

    @Override
    public Stubber doReturn(Object toBeReturned, Object... nextToBeReturned) {
        return MOCKITO_CORE.stubber(Strictness.LENIENT).doReturn(toBeReturned, nextToBeReturned);
    }

    @Override
    public Stubber doCallRealMethod() {
        return null;
    }

    @Override
    public <T> OngoingStubbing<T> when(T methodCall) {
        OngoingStubbingImpl<T> ongoingStubbing = (OngoingStubbingImpl) MOCKITO_CORE.when(methodCall);
        ongoingStubbing.setStrictness(Strictness.LENIENT);
        return ongoingStubbing;
    }
}
