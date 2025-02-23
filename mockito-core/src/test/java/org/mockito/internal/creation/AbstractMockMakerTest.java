/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractMockMakerTest<MM extends MockMaker, C> {

    protected final MM mockMaker;

    private final Class<C> target;

    protected AbstractMockMakerTest(MM mockMaker, Class<C> target) {
        this.mockMaker = mockMaker;
        this.target = target;
    }

    @Test
    public void should_mocks_have_different_interceptors() throws Exception {
        C mockOne = mockMaker.createMock(settingsFor(target), dummyHandler());
        C mockTwo = mockMaker.createMock(settingsFor(target), dummyHandler());

        MockHandler handlerOne = mockMaker.getHandler(mockOne);
        MockHandler handlerTwo = mockMaker.getHandler(mockTwo);

        assertThat(handlerOne).isNotSameAs(handlerTwo);
    }

    @Test
    public void should_reset_mock_and_set_new_handler() throws Throwable {
        MockCreationSettings<C> settings = settingsWithSuperCall(target);
        C proxy = mockMaker.createMock(settings, new MockHandlerImpl<C>(settings));

        MockHandler handler = new MockHandlerImpl<C>(settings);
        mockMaker.resetMock(proxy, handler, settings);
        assertThat(mockMaker.getHandler(proxy)).isSameAs(handler);
    }

    protected static <T> MockCreationSettings<T> settingsFor(
            Class<T> type, Class<?>... extraInterfaces) {
        MockSettingsImpl<T> mockSettings = new MockSettingsImpl<T>();
        mockSettings.setTypeToMock(type);
        if (extraInterfaces.length > 0) mockSettings.extraInterfaces(extraInterfaces);
        return mockSettings;
    }

    protected static <T> MockCreationSettings<T> serializableSettingsFor(
            Class<T> type, SerializableMode serializableMode) {
        MockSettingsImpl<T> mockSettings = new MockSettingsImpl<T>();
        mockSettings.serializable(serializableMode);
        mockSettings.setTypeToMock(type);
        return mockSettings;
    }

    protected static <T> MockCreationSettings<T> settingsWithConstructorFor(Class<T> type) {
        MockSettingsImpl<T> mockSettings = new MockSettingsImpl<T>();
        mockSettings.setTypeToMock(type);
        return mockSettings;
    }

    protected static <T> MockCreationSettings<T> settingsWithSuperCall(Class<T> type) {
        MockSettingsImpl<T> mockSettings = new MockSettingsImpl<T>();
        mockSettings.setTypeToMock(type);
        mockSettings.defaultAnswer(new CallsRealMethods());
        return mockSettings;
    }

    protected static MockHandler dummyHandler() {
        return new DummyMockHandler();
    }

    private static class DummyMockHandler implements MockHandler<Object> {
        public Object handle(Invocation invocation) throws Throwable {
            return null;
        }

        public MockCreationSettings<Object> getMockSettings() {
            return null;
        }

        public InvocationContainer getInvocationContainer() {
            return null;
        }

        public void setAnswersForStubbing(List<Answer<?>> list) {}
    }
}
