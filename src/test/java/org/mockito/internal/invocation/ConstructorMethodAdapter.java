package org.mockito.internal.invocation;

import java.lang.reflect.Constructor;

/**
 * Adapts constructor to method calls needed to work with Mockito API.
 */
public interface ConstructorMethodAdapter {
    Object construct(Constructor constructor, Object... args);
}
