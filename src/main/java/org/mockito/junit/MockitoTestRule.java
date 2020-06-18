/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import org.junit.rules.TestRule;
import org.mockito.Incubating;
import org.mockito.quality.Strictness;

/**
 * Equivalent to {@link MockitoRule}, but inherits a different JUnit4 base interface {@link TestRule}.
 * For more information, please see the documentation on {@link MockitoRule}.
 *
 * @since 3.3.0
 */
public interface MockitoTestRule extends TestRule {

    /**
     * Equivalent to {@link MockitoRule#silent()}.
     *
     * @since 3.3.0
     */
    MockitoTestRule silent();

    /**
     * Equivalent to {@link MockitoRule#strictness(Strictness)}.
     *
     * @since 3.3.0
     */
    @Incubating
    MockitoTestRule strictness(Strictness strictness);
}
