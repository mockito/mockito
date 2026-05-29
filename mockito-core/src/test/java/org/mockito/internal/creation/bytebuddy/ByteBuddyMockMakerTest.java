/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker.TypeMockability;
import org.mockitoutil.TestBase;

public class ByteBuddyMockMakerTest extends TestBase {

    @Mock private SubclassByteBuddyMockMaker delegate;

    @Test
    public void should_delegate_call() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker(delegate);

        CreationSettings<Object> creationSettings = new CreationSettings<Object>();
        MockHandlerImpl<Object> handler = new MockHandlerImpl<Object>(creationSettings);

        Class<?> mockType = mockMaker.createMockType(creationSettings);
        Object mock = mockMaker.createMock(creationSettings, handler);
        MockHandler<?> createdHandler = mockMaker.getHandler(this);
        TypeMockability isTypeMockable = mockMaker.isTypeMockable(Object.class);
        mockMaker.resetMock(this, handler, creationSettings);

        assertNull(mockType);
        assertNull(mock);
        assertNull(createdHandler);
        assertNull(isTypeMockable);
        verify(delegate).createMock(creationSettings, handler);
        verify(delegate).createMockType(creationSettings);
        verify(delegate).getHandler(this);
        verify(delegate).isTypeMockable(Object.class);
        verify(delegate).resetMock(this, handler, creationSettings);
    }
}
