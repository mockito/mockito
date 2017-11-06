/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import java.util.function.Function;
import java.util.function.Supplier;

public class MockitoLambda {

    private static final MockMaker maker = new SubclassByteBuddyMockMaker();

    public static <T> T mock(Class<T> iMethodsClass) {
        MockCreationSettings<T> settings = new MockSettingsImpl<T>().setTypeToMock(iMethodsClass);
        return maker.createMock(settings, new MockitoLambdaHandlerImpl<>(settings));
    }

    public static <R> OngoingStubbingSupplier<R> when(Supplier<R> method) {
        return new OngoingStubbingSupplier<>(method);
    }

    public static <A, R> OngoingStubbingFunction<A, R> when(Function<A, R> method) {
        return new OngoingStubbingFunction<>(method);
    }

    public static <R> OngoingVerificationSupplier<R> verify(Supplier<R> method) {
        return new OngoingVerificationSupplier<>(method);
    }

    public static <A, R> OngoingVerificationFunction<A, R> verify(Function<A, R> method) {
        return new OngoingVerificationFunction<>(method);
    }

    public static <T> LambdaArgumentMatcher<T> any(Class<T> clazz) {
        return (object) -> clazz.isAssignableFrom(object.getClass());
    }

    public static <T> LambdaArgumentMatcher<T> eq(T value) {
        return new Equals<>(value);
    }

    public static <T> LambdaArgumentMatcher<T> isNull() {
        return new Equals<>(null);
    }
}
