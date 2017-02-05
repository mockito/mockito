/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.mock;

import org.mockito.Incubating;

/**
 * Mock serializable style.
 */
@Incubating
public enum SerializableMode {

    /**
     * No serialization.
     */
    NONE,

    /**
     * Basic serializable mode for mock objects. Introduced in Mockito 1.8.1.
     */
    BASIC,

    /**
     * Useful if the mock is deserialized in a different classloader / vm.
     */
    @Incubating
    ACROSS_CLASSLOADERS
}
