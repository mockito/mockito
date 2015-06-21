package org.mockitousage.plugins.switcher;

import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

public class MyMockMaker extends ByteBuddyMockMaker {

    static ThreadLocal<Object> explosive = new ThreadLocal<Object>();

    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        if (explosive.get() != null) {
            throw new RuntimeException("Ka-boom!");
        }
        return super.createMock(settings, handler);
    }

    public MockHandler getHandler(Object mock) {
        return super.getHandler(mock);
    }

    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        super.resetMock(mock, newHandler, settings);
    }

    @Override
    public TypeMockability isTypeMockable(Class<?> type) {
        return super.isTypeMockable(type);
    }
}
