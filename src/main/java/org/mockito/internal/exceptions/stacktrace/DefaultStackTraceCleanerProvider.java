/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.plugins.StackTraceCleanerProvider;

/**
 * by Szczepan Faber, created at: 7/29/12
 */
public class DefaultStackTraceCleanerProvider implements StackTraceCleanerProvider {

    public StackTraceCleaner getStackTraceCleaner(StackTraceCleaner defaultCleaner) {
        return defaultCleaner;
    }
}
