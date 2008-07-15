package org.mockito.internal.stubbing;

import org.mockito.internal.util.MockUtil;

public class MethodSelectorImpl implements MethodSelector {

    private final Object toBeReturned;

    public MethodSelectorImpl(Object toBeReturned) {
        this.toBeReturned = toBeReturned;
    }

    public <T> T when(T mock) {
        MockUtil.getMockHandler(mock).setAnswerForStubbing(new Returns(toBeReturned));
        return mock;
    }
}