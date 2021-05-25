/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.session;

import org.mockito.plugins.MockitoLogger;
import org.mockito.session.MockitoSessionLogger;

class MockitoLoggerAdapter implements MockitoLogger {

    private final MockitoSessionLogger logger;

    MockitoLoggerAdapter(MockitoSessionLogger logger) {
        this.logger = logger;
    }

    @Override
    public void log(Object what) {
        logger.log(String.valueOf(what));
    }
}
