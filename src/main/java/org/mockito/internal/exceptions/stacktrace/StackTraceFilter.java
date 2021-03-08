/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.stacktrace;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.internal.configuration.plugins.Plugins;

public class StackTraceFilter implements Serializable {

    static final long serialVersionUID = -5499819791513105700L;

    private static final StackTraceCleaner CLEANER =
            Plugins.getStackTraceCleanerProvider()
                    .getStackTraceCleaner(new DefaultStackTraceCleaner());

    private static Object STACK_WALKER_INSTANCE;
    private static Method WALK_METHOD;
    private static Method TO_STACKTRACE_ELEMENT;
    private static Method DROP_WHILE;

    static {
        try {
            STACK_WALKER_INSTANCE =
                    Class.forName("java.lang.StackWalker").getMethod("getInstance").invoke(null);
            WALK_METHOD = Class.forName("java.lang.StackWalker").getMethod("walk", Function.class);
            TO_STACKTRACE_ELEMENT =
                    Class.forName("java.lang.StackWalker.StackFrame")
                            .getMethod("toStackTraceElement");
            DROP_WHILE =
                    Class.forName("java.util.stream.Stream")
                            .getMethod("dropWhile", Predicate.class);
        } catch (Exception ignored) {
            // Use the slow computational path for filtering stacktraces if fast path does not exist
            // in JVM
        }
    }

    /**
     * Example how the filter works (+/- means good/bad):
     * [a+, b+, c-, d+, e+, f-, g+] -> [a+, b+, d+, e+, g+]
     * Basically removes all bad from the middle.
     * <strike>If any good are in the middle of bad those are also removed.</strike>
     */
    public StackTraceElement[] filter(StackTraceElement[] target, boolean keepTop) {
        // TODO: profile
        // TODO: investigate "keepTop" commit history - no effect!
        final List<StackTraceElement> filtered = new ArrayList<StackTraceElement>();
        for (StackTraceElement element : target) {
            if (CLEANER.isIn(element)) {
                filtered.add(element);
            }
        }
        StackTraceElement[] result = new StackTraceElement[filtered.size()];
        return filtered.toArray(result);
    }

    /**
     * This filtering strategy makes use of a fast-path computation to retrieve stackTraceElements
     * from a Stacktrace of a Throwable. It does so, by taking advantage of {@link
     * java.lang.StackWalker}.
     *
     * <p>The {@link java.lang.StackWalker} provides a method to efficiently walk a stacktrace,
     * without requiring to compute the full stacktrace. This prevents the JVM from having to
     * generate a full stacktrace, which could potentially be expensive if stacktraces become
     * very large.
     *
     * @param target The throwable target to find the first {@link StackTraceElement} that should
     *     not be filtered out per {@link StackTraceFilter#CLEANER}.
     * @return The first {@link StackTraceElement} outside of the {@link StackTraceFilter#CLEANER}
     */
    public StackTraceElement filterFirst(Throwable target, boolean isInline) {
        AtomicBoolean shouldSkip = new AtomicBoolean(isInline);

        if (STACK_WALKER_INSTANCE != null) {
            try {
                WALK_METHOD.invoke(
                        STACK_WALKER_INSTANCE, getFirstNonMockitoStackTraceElement(shouldSkip));
            } catch (IllegalAccessException | InvocationTargetException ignored) {
                // Fall back to slow path
            }
        }

        // If we can't use the fast path of retrieving stackTraceElements, use the slow path by
        // iterating over the actual stacktrace
        for (StackTraceElement stackTraceElement : target.getStackTrace()) {
            if (CLEANER.isIn(stackTraceElement)) {
                if (shouldSkip.get()) {
                    shouldSkip.set(false);
                } else {
                    return stackTraceElement;
                }
            }
        }
        return null;
    }

    private Function<Stream<Object>, StackTraceElement> getFirstNonMockitoStackTraceElement(
            AtomicBoolean shouldSkip) {
        return (Stream<Object> s) ->
                removeMockitoStackTraceElements(shouldSkip, s)
                        .map(StackTraceFilter::getStackTraceElement)
                        .findFirst()
                        .orElseThrow(
                                () -> {
                                    throw new MockitoException(
                                            "Internal error occurred. Mockito was unable to find the first non-Mockito stackframe.");
                                });
    }

    private Stream<Object> removeMockitoStackTraceElements(
            AtomicBoolean shouldSkip, Stream<Object> s) {
        try {
            return (Stream<Object>)
                    DROP_WHILE.invoke(
                            s,
                            (Predicate<Object>) frame -> shouldDropStackFrame(frame, shouldSkip));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MockitoException(
                    "Internal error occurred. Mockito was unable to find the first non-Mockito stackframe.",
                    e);
        }
    }

    private static StackTraceElement getStackTraceElement(Object frame) {
        try {
            return (StackTraceElement) TO_STACKTRACE_ELEMENT.invoke(frame);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MockitoException(
                    "Internal error occurred. Mockito was unable to find the first non-Mockito stackframe.",
                    e);
        }
    }

    private static boolean shouldDropStackFrame(Object frame, AtomicBoolean shouldSkip) {
        if (CLEANER.isIn(getStackTraceElement(frame))) {
            if (shouldSkip.get()) {
                shouldSkip.set(false);
            } else {
                return false;
            }
        }
        return true;
    }
}
