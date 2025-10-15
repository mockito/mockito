/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Represents a mock of any object construction of the represented type. Within the scope of the
 * mocked construction, the invocation of any interceptor will generate a mock which will be
 * prepared as specified when generating this scope. The mock can also be received via this
 * instance.
 * <p>
 * If the {@link Mock} annotation is used on fields or method parameters of this type, a mocked
 * construction is created instead of a regular mock. The mocked construction is activated and
 * released upon completing any relevant test.
 *
 * @param <T> The type for which the construction is being mocked.
 */
public interface MockedConstruction<T> extends ScopedMock {

    /**
     * Get the constructed mocks.
     *
     * @return the constructed mocks.
     */
    List<T> constructed();

    /**
     * The context for a construction mock.
     */
    interface Context {

        int getCount();

        /**
         * Get the constructor that is invoked during the mock creation.
         *
         * @return the constructor.
         */
        Constructor<?> constructor();

        /**
         * Get the arguments that were passed to the constructor.
         *
         * @return the arguments passed to the constructor, as a list.
         */
        List<?> arguments();
    }

    /**
     * Functional interface that consumes a newly created mock and the mock context.
     * <p>
     * Used to attach behaviours to new mocks.
     *
     * @param <T> the mock type.
     */
    @FunctionalInterface
    interface MockInitializer<T> {

        /**
         * Configure the mock.
         *
         * @param mock the newly created mock.
         * @param context the mock context.
         * @throws Throwable any exception that may be thrown.
         */
        void prepare(T mock, Context context) throws Throwable;
    }
}
