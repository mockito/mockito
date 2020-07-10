/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.mockito.Incubating;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

/**
 * ByteBuddy MockMaker.
 *
 * This class will serve as the programmatic entry point to all mockito internal MockMakers.
 * Currently the default and only mock maker is the subclassing engine, but with enough feedback we can later
 * promote the inlining engine for features like final class/methods mocks.
 *
 * The programmatic API could look like {@code mock(Final.class, withSettings().finalClasses())}.
 */
public class ByteBuddyMockMaker implements ClassCreatingMockMaker {
    private ClassCreatingMockMaker defaultByteBuddyMockMaker = new SubclassByteBuddyMockMaker();

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        return defaultByteBuddyMockMaker.createMock(settings, handler);
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> creationSettings) {
        return defaultByteBuddyMockMaker.createMockType(creationSettings);
    }

    @Override
    public MockHandler getHandler(Object mock) {
        return defaultByteBuddyMockMaker.getHandler(mock);
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        defaultByteBuddyMockMaker.resetMock(mock, newHandler, settings);
    }

    @Override
    @Incubating
    public TypeMockability isTypeMockable(Class<?> type) {
        return defaultByteBuddyMockMaker.isTypeMockable(type);
    }

    @Override
    public <T> StaticMockControl<T> createStaticMock(
            Class<T> type, MockCreationSettings<T> settings, MockHandler handler) {
        return defaultByteBuddyMockMaker.createStaticMock(type, settings, handler);
    }
}
