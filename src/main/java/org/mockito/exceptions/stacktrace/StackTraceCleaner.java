package org.mockito.exceptions.stacktrace;

/**
 * Decides if particular StackTraceElement is excluded from the human-readable stack trace output.
 * Mockito stack trace filtering mechanism uses this information.
 * <p>
 * Excluding an element will make it not show in the cleaned stack trace.
 * Not-excluding an element does not guarantee it will be shown
 * (e.g. it depends on the implementation of
 * Mockito internal {@link org.mockito.internal.exceptions.stacktrace.StackTraceFilter}).
 * <p>
 * The implementations are required to be thread safe. For example, make them stateless.
 * <p>
 * See the default implementation: {@link org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner}.
 *
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
