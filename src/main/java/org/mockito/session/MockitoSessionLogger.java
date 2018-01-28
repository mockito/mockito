/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.session;

import org.mockito.Incubating;
import org.mockito.MockitoSession;

/**
 * Logger for hints emitted when finishing mocking for a {@link MockitoSession}.
 *
 * @since 2.13.4
 */
@Incubating
public interface MockitoSessionLogger {

    /**
     * Logs the emitted hint.
     *
     * @param hint to log; never {@code null}
     */
    @Incubating
    void log(String hint);

}
