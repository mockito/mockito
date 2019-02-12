/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubbing;

import java.io.IOException;
import java.io.Serializable;

import static org.mockito.Mockito.withSettings;

/**
 * Returning deep stub implementation.
 *
 * Will return previously created mock if the invocation matches.
 *
 * <p>Supports nested generic information, with this answer you can write code like this :
 *
 * <pre class="code"><code class="java">
 *     interface GenericsNest&lt;K extends Comparable&lt;K&gt; & Cloneable&gt; extends Map&lt;K, Set&lt;Number&gt;&gt; {}
 *
 *     GenericsNest&lt;?&gt; mock = mock(GenericsNest.class, new ReturnsGenericDeepStubs());
 *     Number number = mock.entrySet().iterator().next().getValue().iterator().next();
 * </code></pre>
 * </p>
 *
 * @see org.mockito.Mockito#RETURNS_DEEP_STUBS
 * @see org.mockito.Answers#RETURNS_DEEP_STUBS
 */
public class ReturnsDeepStubs implements Answer<Object>, Serializable {

    private static final long serialVersionUID = -7105341425736035847L;

    public Object answer(InvocationOnMock invocation) throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata =
                actualParameterizedType(invocation.getMock()).resolveGenericReturnType(invocation.getMethod());

        Class<?> rawType = returnTypeGenericMetadata.rawType();
        if (!mockitoCore().isTypeMockable(rawType)) {
            return delegate().returnValueFor(rawType);
        }

        // When dealing with erasured generics, we only receive the Object type as rawType. At this
        // point, there is nothing to salvage for Mockito. Instead of trying to be smart and generate
        // a mock that would potentially match the return signature, instead return `null`. This
        // is valid per the CheckCast JVM instruction and is better than causing a ClassCastException
        // on runtime.
        if (rawType.equals(Object.class)) {
            return null;
        }

        return deepStub(invocation, returnTypeGenericMetadata);
    }

    private Object deepStub(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata) throws Throwable {
        InvocationContainerImpl container = MockUtil.getInvocationContainer(invocation.getMock());

        // matches invocation for verification
        // TODO why don't we do container.findAnswer here?
        for (Stubbing stubbing : container.getStubbingsDescending()) {
            if (container.getInvocationForStubbing().matches(stubbing.getInvocation())) {
                return stubbing.answer(invocation);
            }
        }

        // record deep stub answer
        StubbedInvocationMatcher stubbing = recordDeepStubAnswer(
                newDeepStubMock(returnTypeGenericMetadata, invocation.getMock()),
                container
        );

        // deep stubbing creates a stubbing and immediately uses it
        // so the stubbing is actually used by the same invocation
        stubbing.markStubUsed(stubbing.getInvocation());

        return stubbing.answer(invocation);
    }

    /**
     * Creates a mock using the Generics Metadata.
     *
     * <li>Finally as we want to mock the actual type, but we want to pass along the contextual generics meta-data
     * that was resolved for the current return type, for this to happen we associate to the mock an new instance of
     * {@link ReturnsDeepStubs} answer in which we will store the returned type generic metadata.
     *
     * @param returnTypeGenericMetadata The metadata to use to create the new mock.
     * @param parentMock The parent of the current deep stub mock.
     * @return The mock
     */
    private Object newDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata, Object parentMock) {
        MockCreationSettings parentMockSettings = MockUtil.getMockSettings(parentMock);
        return mockitoCore().mock(
                returnTypeGenericMetadata.rawType(),
                withSettingsUsing(returnTypeGenericMetadata, parentMockSettings)
        );
    }

    private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata, MockCreationSettings parentMockSettings) {
        MockSettings mockSettings = returnTypeGenericMetadata.hasRawExtraInterfaces() ?
                withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())
                : withSettings();

        return propagateSerializationSettings(mockSettings, parentMockSettings)
                .defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    private MockSettings propagateSerializationSettings(MockSettings mockSettings, MockCreationSettings parentMockSettings) {
        return mockSettings.serializable(parentMockSettings.getSerializableMode());
    }

    private ReturnsDeepStubs returnsDeepStubsAnswerUsing(final GenericMetadataSupport returnTypeGenericMetadata) {
        return new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
    }

    private StubbedInvocationMatcher recordDeepStubAnswer(final Object mock, InvocationContainerImpl container) {
        DeeplyStubbedAnswer answer = new DeeplyStubbedAnswer(mock);
        return container.addAnswer(answer, false, null);
    }

    protected GenericMetadataSupport actualParameterizedType(Object mock) {
        CreationSettings mockSettings = (CreationSettings) MockUtil.getMockHandler(mock).getMockSettings();
        return GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock());
    }


    private static class ReturnsDeepStubsSerializationFallback extends ReturnsDeepStubs implements Serializable {
        @SuppressWarnings("serial") // not gonna be serialized
        private final GenericMetadataSupport returnTypeGenericMetadata;

        public ReturnsDeepStubsSerializationFallback(GenericMetadataSupport returnTypeGenericMetadata) {
            this.returnTypeGenericMetadata = returnTypeGenericMetadata;
        }

        @Override
        protected GenericMetadataSupport actualParameterizedType(Object mock) {
            return returnTypeGenericMetadata;
        }
        private Object writeReplace() throws IOException {
            return Mockito.RETURNS_DEEP_STUBS;
        }
    }


    private static class DeeplyStubbedAnswer implements Answer<Object>, Serializable {
        @SuppressWarnings("serial") // serialization will fail with a nice message if mock not serializable
        private final Object mock;

        DeeplyStubbedAnswer(Object mock) {
            this.mock = mock;
        }
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return mock;
        }
    }


    private static MockitoCore mockitoCore() {
        return LazyHolder.MOCKITO_CORE;
    }

    private static ReturnsEmptyValues delegate() {
        return LazyHolder.DELEGATE;
    }

    private static class LazyHolder {
        private static final MockitoCore MOCKITO_CORE = new MockitoCore();
        private static final ReturnsEmptyValues DELEGATE = new ReturnsEmptyValues();
    }
}
