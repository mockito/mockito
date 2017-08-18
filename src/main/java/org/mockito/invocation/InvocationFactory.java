package org.mockito.invocation;

import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Provides means to create instances of objects that are not intended to be subclassed by the users.
 * Needed for framework integrations, for example for PowerMock who can use it to implement static mocking.
 */
public interface InvocationFactory {

    /**
     * Creates instance of an {@link Invocation} object.
     *
     * @param target the mock object the method is invoked on.
     * @param settings creation settings of the mock object.
     * @param method java method invoked on mock.
     * @param realMethod real method behavior. Needed for spying on invoking real behavior on mock objects.
     * @param args the java method arguments
     *
     * @return invocation instance
     */
    Invocation createInvocation(Object target, MockCreationSettings settings, Method method, Callable realMethod, Object... args);
}
