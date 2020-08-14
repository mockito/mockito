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
@Incubating
public interface MockedConstruction<T> extends ScopedMock {

    List<T> constructed();

    interface Context {

        int getCount();

        Constructor<?> constructor();

        List<?> arguments();
    }

    interface MockInitializer<T> {

        void prepare(T mock, Context context) throws Throwable;
    }
}
