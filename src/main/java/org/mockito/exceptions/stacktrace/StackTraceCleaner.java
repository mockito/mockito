/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.stacktrace;

/**
 * Decides if particular StackTraceElement is excluded from the human-readable stack trace output.
 * Mockito stack trace filtering mechanism uses this information.
 * <p>
 * Excluding an element will make it not show in the cleaned stack trace.
 * Not-excluding an element does not guarantee it will be shown (e.g. it depends on the implementation of
 * {@linkplain org.mockito.internal.exceptions.stacktrace.StackTraceFilter Mockito internal cleaner}).
 * <p>
 * The implementations are required to be thread safe ; for example, make them stateless.
 * <p>
 * See also the {@linkplain org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner Mockito default implementation}
 */
public interface StackTraceCleaner {

    /**
     * Decides if element is included.
     *
     * @param candidate element of the actual stack trace
     * @return whether the element should be excluded from cleaned stack trace.
     */
    boolean isIn(StackTraceElement candidate);
}
