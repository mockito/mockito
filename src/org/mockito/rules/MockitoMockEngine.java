package org.mockito.rules;

import org.mockito.Mockito;

public final class MockitoMockEngine implements MockEngine {

    private static class Holder {
        public static final MockitoMockEngine INSTANCE = new MockitoMockEngine();
    }

    public static MockitoMockEngine getInstance() {
        return Holder.INSTANCE;
    }

    private MockitoMockEngine() {

    }

    @Override
    public <T> T mock(final Class<T> type) {
        return Mockito.mock(type);
    }

}
