/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.plugins;

import org.mockito.Incubating;
import org.mockito.MockitoFramework;

/**
 * Extension to {@link MockMaker} for mock makers that changes inline method implementations
 * and need keep track of created mock objects.
 * <p>
 * Mockito's default inline mock maker keeps track of created mock objects via weak reference map.
 * This poses a risk of memory leaks in certain scenarios
 * (issue <a href="https://github.com/mockito/mockito/pull/1619">#1619</a>).
 * There is no clean way to tackle those problems at the moment.
 * Hence, {@code InlineMockMaker} interface exposes methods to explicitly clear mock references.
 * Those methods are called by {@link MockitoFramework#clearInlineMocks()}.
 * When the user encounters a leak, he can mitigate the problem with {@link MockitoFramework#clearInlineMocks()}.
 * <p>
 * {@code InlineMockMaker} is for expert users and framework integrators, when custom inline mock maker is in use.
 * If you have a custom {@link MockMaker} that keeps track of mock objects,
 * please have your mock maker implement {@code InlineMockMaker} interface.
 * This way, it can participate in {@link MockitoFramework#clearInlineMocks()} API.
 *
 * @since 2.25.0
 */
@Incubating
public interface InlineMockMaker extends MockMaker {

    /**
     * Clean up internal state for specified {@code mock}. You may assume there won't be any interaction to the specific
     * mock after this is called.
     *
     * @param mock the mock instance whose internal state is to be cleaned.
     * @since 2.25.0
     */
    @Incubating
    void clearMock(Object mock);

    /**
     * Cleans up internal state for all existing mocks. You may assume there won't be any interaction to mocks created
     * previously after this is called.
     *
     * @since 2.25.0
     */
    @Incubating
    void clearAllMocks();

}
