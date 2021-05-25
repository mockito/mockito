/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.logger;

import java.util.ArrayList;
import java.util.List;

import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.plugins.MockitoLogger;

public class MyMockitoLogger implements MockitoLogger {
    private static final ThreadLocal<Boolean> enabled = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    private static final ThreadLocal<List<Object>> loggedItems = new ThreadLocal<List<Object>>() {
        @Override
        protected List<Object> initialValue() {
            return new ArrayList<Object>();
        }
    };

    private final MockitoLogger defaultLogger = new ConsoleMockitoLogger();

    @Override
    public void log(Object what) {
        if (enabled.get()) {
            loggedItems.get().add(what);
        } else {
            defaultLogger.log(what);
        }
    }

    static void enable() {
        enabled.set(true);
    }

    static List<Object> getLoggedItems() {
        return loggedItems.get();
    }

    static void clear() {
        enabled.remove();
        loggedItems.remove();
    }
}
