/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

public class SimpleMockitoLogger implements MockitoLogger {

    private StringBuilder loggedInfo = new StringBuilder();

    public void log(Object what) {
        loggedInfo.append(what);
    }

    public String getLoggedInfo() {
        return loggedInfo.toString();
    }

    public boolean isEmpty() {
        return loggedInfo.length() == 0;
    }

    public SimpleMockitoLogger clear() {
        loggedInfo = new StringBuilder();
        return this;
    }
}