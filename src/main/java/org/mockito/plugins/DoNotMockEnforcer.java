/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import java.util.Optional;

/**
 * Enforcer that is applied to every type in the type hierarchy of the class-to-be-mocked.
 */
public interface DoNotMockEnforcer {

    /**
     * If this type is allowed to be mocked. Note that Mockito performs traversal of the type
     * hierarchy. Implementations of this class should therefore not perform type traversal
     * themselves but rely on Mockito.
     *
     * @param type The type to check.
     * @return Whether this type can be mocked.
     */
    Optional<String> allowMockType(Class<?> type);
}
