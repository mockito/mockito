package org.mockito;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class MockitoLambda {

    private static final MockMaker maker = new SubclassByteBuddyMockMaker();

    public static <T> T mock(Class<T> iMethodsClass) {
        MockCreationSettings<T> settings = new MockSettingsImpl<T>().setTypeToMock(iMethodsClass);
        return maker.createMock(settings, new MockitoLambdaHandlerImpl<>(settings));
    }

    public static <R> OngoingStubbingCallable<R> when(Callable<R> method) {
        return new OngoingStubbingCallable<>(method);
    }

    public static <A, R> OngoingStubbingFunction<A, R> when(Function<A, R> method) {
        return new OngoingStubbingFunction<>(method);
    }

    public static <T> LambdaArgumentMatcher<T> any(Class<T> clazz) {
        return (object) -> clazz.isAssignableFrom(object.getClass());
    }
}
