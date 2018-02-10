/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.session;

import org.mockito.Incubating;
import org.mockito.MockitoSession;

/**
 * Logger for {@linkplain org.mockito.quality.MockitoHint hints} emitted when
 * finishing mocking for a {@link MockitoSession}.
 * <p>
 * This class is intended to be used by framework integrations, e.g. JUnit. When using
 * {@link MockitoSession} directly, you'll probably not need it.
 *
 * @since 2.15.0
 */
@Incubating
public interface MockitoSessionLogger {

    /**
     * Logs the hint.
     *
     * @param hint to log; never {@code null}
     */
    @Incubating
    void log(String hint);

}
