/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.mockito.MockitoFramework;
import org.mockito.mock.MockCreationSettings;

/**
 * Available via {@link MockitoFramework#getInvocationFactory()}.
 * Provides means to create instances of {@link Invocation} objects.
 * Useful for framework integrations that need to programmatically simulate method calls on mock objects.
 * To simulate a method call on mock, one needs an instance of {@link Invocation}.
 * <p>
 * Please don't provide your own implementation of {@link Invocation} type.
 * Mockito team needs flexibility to add new methods to this interface if we need to.
 * If you integrate Mockito framework and you need an instance of {@link Invocation}, use {@link #createInvocation(Object, MockCreationSettings, Method, RealMethodBehavior, Object...)}.
 *
 * @since 2.10.0
 */
public interface InvocationFactory {

    /**
     * Behavior of the real method.
     *
     * @since 2.14.0
     */
    interface RealMethodBehavior<R> extends Serializable {
        R call() throws Throwable;
    }

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
     * @since 2.14.0
     */
    Invocation createInvocation(
            Object target,
            MockCreationSettings settings,
            Method method,
            RealMethodBehavior realMethod,
            Object... args);
}
