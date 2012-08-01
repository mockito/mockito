/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.internal.util.reflection.MockitoGenericMetadata;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;

/**
 * Returning deep stub implementation.
 *
 * Will return previously created mock if the invocation matches.
 *
 * @see Mockito#RETURNS_DEEP_STUBS
 * @see org.mockito.Answers#RETURNS_DEEP_STUBS
 */
public class ReturnsDeepStubs implements Answer<Object>, Serializable {
    
    private static final long serialVersionUID = -6926328908792880098L;
    
    private ReturnsEmptyValues delegate = new ReturnsEmptyValues();

    private MockitoLogger logger = new ConsoleMockitoLogger();

    public Object answer(InvocationOnMock invocation) throws Throwable {
        Class<?> clz = invocation.getMethod().getReturnType();

        if (!new MockCreationValidator().isTypeMockable(clz)) {
            return delegate.answer(invocation);
        }

        return getMock(invocation);
    }

    private Object getMock(InvocationOnMock invocation) throws Throwable {
    	InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
    	InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        // matches invocation for verification
        for (StubbedInvocationMatcher stubbedInvocationMatcher : container.getStubbedInvocations()) {
    		if(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())) {
    			return stubbedInvocationMatcher.answer(invocation);
    		}
		}

        // deep stub
        return recordDeepStubMock(invocation, container);
    }

    private Object recordDeepStubMock(InvocationOnMock invocation, InvocationContainerImpl container) throws Throwable {
        final Object mock = createGenericsAwareMock(invocation);

        container.addAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mock;
            }
        }, false);

        return mock;
    }

    private Object createGenericsAwareMock(InvocationOnMock invocation) throws Throwable {
        Type genericReturnType = invocation.getMethod().getGenericReturnType();

        if (genericReturnType instanceof Class) {
            return mock((Class<?>) genericReturnType, this);
        }

        MockitoGenericMetadata returnTypeGenericMetadata =
                actualParameterizedType(invocation.getMock()).resolveGenericReturnType(invocation.getMethod());

        Object mock = returnTypeGenericMetadata.toMock(this);
        if (mock == null) {
            return delegate.returnValueFor(returnTypeGenericMetadata.rawType());
        }
        return mock;
    }

    private MockitoGenericMetadata actualParameterizedType(Object mock) {
        CreationSettings mockSettings = (CreationSettings) new MockUtil().getMockHandler(mock).getMockSettings();
        return mockSettings.getMockitoGenericMetadata();
    }
}
