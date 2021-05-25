/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockitoutil.TestBase;

public class ByteBuddyMockMakerTest extends TestBase {

    @Mock private SubclassByteBuddyMockMaker delegate;

    @Test
    public void should_delegate_call() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker(delegate);

        CreationSettings<Object> creationSettings = new CreationSettings<Object>();
        MockHandlerImpl<Object> handler = new MockHandlerImpl<Object>(creationSettings);

        mockMaker.createMockType(creationSettings);
        mockMaker.createMock(creationSettings, handler);
        mockMaker.getHandler(this);
        mockMaker.isTypeMockable(Object.class);
        mockMaker.resetMock(this, handler, creationSettings);

        verify(delegate).createMock(creationSettings, handler);
        verify(delegate).createMockType(creationSettings);
        verify(delegate).getHandler(this);
        verify(delegate).isTypeMockable(Object.class);
        verify(delegate).resetMock(this, handler, creationSettings);
    }
}
