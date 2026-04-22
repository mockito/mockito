/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker.ConstructionMockControl;
import org.mockito.plugins.MockMaker.StaticMockControl;
import org.mockito.plugins.MockMaker.TypeMockability;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

public class InlineByteBuddyMockMakerTest extends TestBase {

    @Mock private InlineDelegateByteBuddyMockMaker delegate;

    @Test
    public void should_delegate_call() {
        InlineByteBuddyMockMaker mockMaker = new InlineByteBuddyMockMaker(delegate);

        CreationSettings<Object> creationSettings = new CreationSettings<Object>();
        MockHandlerImpl<Object> handler = new MockHandlerImpl<Object>(creationSettings);

        Class<?> mockType = mockMaker.createMockType(creationSettings);
        Object mock = mockMaker.createMock(creationSettings, handler);
        StaticMockControl<Object> staticMock =
                mockMaker.createStaticMock(Object.class, creationSettings, handler);
        ConstructionMockControl<Object> constructionMock =
                mockMaker.createConstructionMock(Object.class, null, null, null);
        MockHandler createdHandler = mockMaker.getHandler(this);
        TypeMockability isTypeMockable = mockMaker.isTypeMockable(Object.class);
        mockMaker.resetMock(this, handler, creationSettings);
        mockMaker.clearMock(this);
        mockMaker.clearAllMocks();
        mockMaker.clearAllCaches();

        assertNull(mockType);
        assertNull(mock);
        assertNull(staticMock);
        assertNull(constructionMock);
        assertNull(createdHandler);
        assertNull(isTypeMockable);
        verify(delegate).createMock(creationSettings, handler);
        verify(delegate).createStaticMock(Object.class, creationSettings, handler);
        verify(delegate).createConstructionMock(Object.class, null, null, null);
        verify(delegate).createMockType(creationSettings);
        verify(delegate).getHandler(this);
        verify(delegate).isTypeMockable(Object.class);
        verify(delegate).resetMock(this, handler, creationSettings);
        verify(delegate).clearMock(this);
        verify(delegate).clearAllMocks();
        verify(delegate).clearAllCaches();
    }
}
