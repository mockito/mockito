/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.plugins;

import org.mockito.Incubating;

/**
 * Extension to {@link MockMaker} for mock makers that changes inline method implementations.
 * @since 2.24.8
 */
@Incubating
public interface InlineMockMaker extends MockMaker {
    /**
     * Clean up internal state for specified {@code mock}. You may assume there won't be any interaction to the specific
     * mock after this is called.
     *
     * @param mock the mock instance whose internal state is to be cleaned.
     * @since 2.24.8
     */
    @Incubating
    void clearMock(Object mock);

    /**
     * Cleans up internal state for all existing mocks. You may assume there won't be any interaction to mocks created
     * previously after this is called.
     *
     * @since 2.24.8
     */
    @Incubating
    void clearAllMocks();

}
