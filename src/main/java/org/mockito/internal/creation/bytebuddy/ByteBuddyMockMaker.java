/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.mockito.Incubating;
import org.mockito.MockedConstruction;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

import java.util.Optional;
import java.util.function.Function;

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
    private final SubclassByteBuddyMockMaker subclassByteBuddyMockMaker;

    public ByteBuddyMockMaker() {
        try {
            subclassByteBuddyMockMaker = new SubclassByteBuddyMockMaker();
        } catch (NoClassDefFoundError e) {
            Reporter.missingByteBuddyDependency(e);
            throw e;
        }
    }

    ByteBuddyMockMaker(SubclassByteBuddyMockMaker subclassByteBuddyMockMaker) {
        this.subclassByteBuddyMockMaker = subclassByteBuddyMockMaker;
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        return subclassByteBuddyMockMaker.createMock(settings, handler);
    }

    @Override
    public <T> Optional<T> createSpy(
            MockCreationSettings<T> settings, MockHandler handler, T object) {
        return subclassByteBuddyMockMaker.createSpy(settings, handler, object);
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> creationSettings) {
        return subclassByteBuddyMockMaker.createMockType(creationSettings);
    }

    @Override
    public MockHandler getHandler(Object mock) {
        return subclassByteBuddyMockMaker.getHandler(mock);
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        subclassByteBuddyMockMaker.resetMock(mock, newHandler, settings);
    }

    @Override
    @Incubating
    public TypeMockability isTypeMockable(Class<?> type) {
        return subclassByteBuddyMockMaker.isTypeMockable(type);
    }

    @Override
    public <T> StaticMockControl<T> createStaticMock(
            Class<T> type, MockCreationSettings<T> settings, MockHandler handler) {
        return subclassByteBuddyMockMaker.createStaticMock(type, settings, handler);
    }

    @Override
    public <T> ConstructionMockControl<T> createConstructionMock(
            Class<T> type,
            Function<MockedConstruction.Context, MockCreationSettings<T>> settingsFactory,
            Function<MockedConstruction.Context, MockHandler<T>> handlerFactory,
            MockedConstruction.MockInitializer<T> mockInitializer) {
        return subclassByteBuddyMockMaker.createConstructionMock(
                type, settingsFactory, handlerFactory, mockInitializer);
    }

    @Override
    public void clearAllCaches() {
        subclassByteBuddyMockMaker.clearAllCaches();
    }
}
