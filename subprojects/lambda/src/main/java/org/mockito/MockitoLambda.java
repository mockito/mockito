/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import org.mockito.verification.VerificationMode;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.Answers.RETURNS_DEFAULTS;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class MockitoLambda {

    private static final MockMaker maker = Plugins.getMockMaker();

    public static <T> T mock(Class<T> iMethodsClass) {
        MockSettingsImpl<T> settings = new MockSettingsImpl<T>();
        settings.defaultAnswer(RETURNS_DEFAULTS);
        settings.setTypeToMock(iMethodsClass);
        return maker.createMock(settings, new MockitoLambdaHandlerImpl<>(settings));
    }

    @SuppressWarnings("unchecked")
    public static <T> T spy(T object) {
        MockCreationSettings<T> settings = new MockSettingsImpl<T>()
            .spiedInstance(object)
            .defaultAnswer(Answers.CALLS_REAL_METHODS)
            .build((Class<T>) object.getClass());
        T mock = maker.createMock(settings, new MockitoLambdaHandlerImpl<>(settings));
        new LenientCopyTool().copyToMock(object, mock);
        return mock;
    }

    public static <R> OngoingStubbingSupplier<R> when(Supplier<R> method) {
        return new OngoingStubbingSupplier<>(method);
    }

    public static <A, R> OngoingStubbingFunction<A, R> when(Function<A, R> method) {
        return new OngoingStubbingFunction<>(method);
    }

    public static <A> OngoingStubbingConsumer<A> when(Consumer<A> method) {
        return new OngoingStubbingConsumer<>(method);
    }

    public static <R> OngoingVerificationSupplier<R> verify(Supplier<R> method) {
        return verify(method, times(1));
    }

    public static <R> OngoingVerificationSupplier<R> verify(Supplier<R> method, VerificationMode mode) {
        return new OngoingVerificationSupplier<>(method, mode);
    }

    public static <A, R> OngoingVerificationFunction<A, R> verify(Function<A, R> method) {
        return verify(method, times(1));
    }

    public static <A, R> OngoingVerificationFunction<A, R> verify(Function<A, R> method, VerificationMode mode) {
        return new OngoingVerificationFunction<>(method, mode);
    }

    public static <T> LambdaArgumentMatcher<T> any() {
        return new Any<>();
    }

    public static <T> LambdaArgumentMatcher<T> any(Class<T> clazz) {
        return new AnyClass<>(clazz);
    }

    public static <T> LambdaArgumentMatcher<T> eq(T value) {
        return new Equals<>(value);
    }

    public static <T> LambdaArgumentMatcher<T> isNull() {
        return new Equals<>(null);
    }
}
