/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockitoutil.TestBase;

public class InlineByteBuddyMockMakerTest extends TestBase {

    @Mock private InlineDelegateByteBuddyMockMaker delegate;

    @Test
    public void should_delegate_call() {
        InlineByteBuddyMockMaker mockMaker = new InlineByteBuddyMockMaker(delegate);

        CreationSettings<Object> creationSettings = new CreationSettings<Object>();
        MockHandlerImpl<Object> handler = new MockHandlerImpl<Object>(creationSettings);

        mockMaker.createMockType(creationSettings);
        mockMaker.createMock(creationSettings, handler);
        mockMaker.createStaticMock(Object.class, creationSettings, handler);
        mockMaker.createConstructionMock(Object.class, null, null, null);
        mockMaker.getHandler(this);
        mockMaker.isTypeMockable(Object.class);
        mockMaker.resetMock(this, handler, creationSettings);
        mockMaker.clearMock(this);
        mockMaker.clearAllMocks();
        mockMaker.clearAllCaches();

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

    @Test
    public void should_delegate_suppress_static_initialization() {
        InlineByteBuddyMockMaker mockMaker = new InlineByteBuddyMockMaker(delegate);

        List<String> classNames = Arrays.asList("com.example.Foo", "com.example.Bar");
        mockMaker.suppressStaticInitializationFor(classNames);

        verify(delegate).suppressStaticInitializationFor(classNames);
    }

    @Test
    public void should_delegate_restore_static_initialization() {
        InlineByteBuddyMockMaker mockMaker = new InlineByteBuddyMockMaker(delegate);

        List<String> classNames = Collections.singletonList("com.example.Foo");
        mockMaker.restoreStaticInitializationFor(classNames);

        verify(delegate).restoreStaticInitializationFor(classNames);
    }
}
