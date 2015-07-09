package org.mockito.plugins;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;

/**
 * An extension point to register custom {@link StackTraceCleaner}.
 * You can replace Mockito's default StackTraceCleaner.
 * You can also 'enhance' Mockito's default behavior
 * because the default cleaner is passed as parameter to the method.
 * <p>
 * Registering custom StackTraceCleaner is done in similar manner as the {@link MockMaker} implementation.
 * <p>
 * See the default implementation: {@link org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider}
 */
public interface StackTraceCleanerProvider {

    /**
     * Allows configuring custom StackTraceCleaner.
     *
     * @param defaultCleaner - Mockito's default StackTraceCleaner
     * @return StackTraceCleaner to use
     */
    StackTraceCleaner getStackTraceCleaner(StackTraceCleaner defaultCleaner);
}