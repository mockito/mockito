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