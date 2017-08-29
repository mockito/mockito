package org.mockito.invocation;

import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Provides means to create instances of {@link Invocation} objects.
 * Needed for framework integrations, for example for PowerMock who can use it to implement static mocking.
 * <p>
 * Please don't provide your own implementation of {@link Invocation} type.
 * Mockito team needs flexibility to add new methods to this interface if we need to.
 * If you integrate Mockito framework and you need an instance of {@link Invocation}, use {@link #createInvocation(Object, MockCreationSettings, Method, Callable, Object...)}.
 *
 * @since 2.10.0
 */
public interface InvocationFactory {

    /**
     * Creates instance of an {@link Invocation} object.
     * This method is useful for framework integrators to programmatically simulate method calls on mocks using {@link MockHandler}.
     * It enables advanced framework integrations.
     *
     * @param target the mock object the method is invoked on.
     * @param settings creation settings of the mock object.
     * @param method java method invoked on mock.
     * @param realMethod real method behavior. Needed for spying / invoking real behavior on mock objects.
     * @param args the java method arguments
     *
     * @return invocation instance
     * @since 2.10.0
     */
    Invocation createInvocation(Object target, MockCreationSettings settings, Method method, Callable realMethod, Object... args);
}
