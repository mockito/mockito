/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.framework;

import org.mockito.MockitoFramework;
import org.mockito.exceptions.misusing.DisabledMockException;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.listeners.VerificationStartedListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.MockType;
import org.mockito.mock.SerializableMode;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * Throws {@link DisabledMockException} when a mock is accessed after it has been disabled by
 * {@link MockitoFramework#clearInlineMocks()}.
 */
public class DisabledMockHandler implements MockHandler {
    public static MockHandler HANDLER = new DisabledMockHandler();

    private DisabledMockHandler() {
        // private, use HANDLER instead
    }

    @Override
    public Object handle(Invocation invocation) {
        throw new DisabledMockException();
    }

    @Override
    public MockCreationSettings getMockSettings() {
        return DisabledMockCreationSettings.INSTANCE;
    }

    @Override
    public InvocationContainer getInvocationContainer() {
        return null;
    }

    private static class DisabledMockCreationSettings
            implements Serializable, MockCreationSettings {
        private static DisabledMockCreationSettings INSTANCE = new DisabledMockCreationSettings();

        private DisabledMockCreationSettings() {
            // USE INSTANCE
        }

        @Override
        public Class getTypeToMock() {
            throw new DisabledMockException();
        }

        @Override
        public Type getGenericTypeToMock() {
            throw new DisabledMockException();
        }

        @Override
        public Set<Class<?>> getExtraInterfaces() {
            throw new DisabledMockException();
        }

        @Override
        public MockName getMockName() {
            throw new DisabledMockException();
        }

        @Override
        public Answer<?> getDefaultAnswer() {
            throw new DisabledMockException();
        }

        @Override
        public Object getSpiedInstance() {
            throw new DisabledMockException();
        }

        @Override
        public boolean isSerializable() {
            throw new DisabledMockException();
        }

        @Override
        public SerializableMode getSerializableMode() {
            throw new DisabledMockException();
        }

        @Override
        public boolean isStubOnly() {
            throw new DisabledMockException();
        }

        @Override
        public boolean isStripAnnotations() {
            throw new DisabledMockException();
        }

        @Override
        public List<StubbingLookupListener> getStubbingLookupListeners() {
            throw new DisabledMockException();
        }

        @Override
        public List<InvocationListener> getInvocationListeners() {
            throw new DisabledMockException();
        }

        @Override
        public List<VerificationStartedListener> getVerificationStartedListeners() {
            throw new DisabledMockException();
        }

        @Override
        public boolean isUsingConstructor() {
            throw new DisabledMockException();
        }

        @Override
        public Object[] getConstructorArgs() {
            throw new DisabledMockException();
        }

        @Override
        public Object getOuterClassInstance() {
            throw new DisabledMockException();
        }

        @Override
        public boolean isLenient() {
            throw new DisabledMockException();
        }

        @Override
        public Strictness getStrictness() {
            throw new DisabledMockException();
        }

        @Override
        public String getMockMaker() {
            throw new DisabledMockException();
        }

        @Override
        public MockType getMockType() {
            throw new DisabledMockException();
        }
    }
}
