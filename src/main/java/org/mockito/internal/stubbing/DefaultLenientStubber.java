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
        return stubber().doThrow(toBeThrown);
    }

    @Override
    public Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return stubber().doThrow(toBeThrown);
    }

    @Override
    public Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        return stubber().doThrow(toBeThrown, nextToBeThrown);
    }

    @Override
    public Stubber doAnswer(Answer answer) {
        return stubber().doAnswer(answer);
    }

    @Override
    public Stubber doNothing() {
        return stubber().doNothing();
    }

    @Override
    public Stubber doReturn(Object toBeReturned) {
        return stubber().doReturn(toBeReturned);
    }

    @Override
    public Stubber doReturn(Object toBeReturned, Object... nextToBeReturned) {
        return stubber().doReturn(toBeReturned, nextToBeReturned);
    }

    @Override
    public Stubber doCallRealMethod() {
        return stubber().doCallRealMethod();
    }

    @Override
    public <T> OngoingStubbing<T> when(T methodCall) {
        OngoingStubbingImpl<T> ongoingStubbing = (OngoingStubbingImpl) MOCKITO_CORE.when(methodCall);
        ongoingStubbing.setStrictness(Strictness.LENIENT);
        return ongoingStubbing;
    }

    private static Stubber stubber() {
        return MOCKITO_CORE.stubber(Strictness.LENIENT);
    }
}
