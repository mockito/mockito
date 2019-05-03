/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.exceptions.stacktrace;

import java.lang.reflect.Method;
import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.internal.configuration.plugins.Plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StackTraceFilter implements Serializable {

    static final long serialVersionUID = -5499819791513105700L;

    private static final StackTraceCleaner CLEANER =
            Plugins.getStackTraceCleanerProvider().getStackTraceCleaner(new DefaultStackTraceCleaner());

    private static Object JAVA_LANG_ACCESS;
    private static Method GET_STACK_TRACE_ELEMENT;

    static {
        try {
            JAVA_LANG_ACCESS =
                Class.forName("sun.misc.SharedSecrets")
                    .getMethod("getJavaLangAccess")
                    .invoke(null);
            GET_STACK_TRACE_ELEMENT =
                Class.forName("sun.misc.JavaLangAccess")
                    .getMethod("getStackTraceElement", Throwable.class, int.class);
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
        //TODO: profile
        //TODO: investigate "keepTop" commit history - no effect!
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
     * sun.misc.SharedSecrets} and {@link sun.misc.JavaLangAccess}.
     *
     * <p>The {@link sun.misc.SharedSecrets} provides a method to obtain an instance of an {@link
     * sun.misc.JavaLangAccess}. The latter class has a method to fast-path into {@link
     * Throwable#getStackTrace()} and retrieve a single {@link StackTraceElement}. This prevents the
     * JVM from having to generate a full stacktrace, which could potentially be expensive if
     * stacktraces become very large.
     *
     * @param target The throwable target to find the first {@link StackTraceElement} that should
     *     not be filtered out per {@link StackTraceFilter#CLEANER}.
     * @return The first {@link StackTraceElement} outside of the {@link StackTraceFilter#CLEANER}
     */
    public StackTraceElement filterFirst(Throwable target, boolean isInline) {
        boolean shouldSkip = isInline;

        if (GET_STACK_TRACE_ELEMENT != null) {
            int i = 0;

            // The assumption here is that the CLEANER filter will not filter out every single
            // element. However, since we don't want to compute the full length of the stacktrace,
            // we don't know the upper boundary. Therefore, simply increment the counter and go as
            // far as we have to go, assuming that we get there. If, in the rare occassion, we
            // don't, we fall back to the old slow path.
            while (true) {
                try {
                    StackTraceElement stackTraceElement =
                        (StackTraceElement)
                            GET_STACK_TRACE_ELEMENT.invoke(JAVA_LANG_ACCESS, target, i);

                    if (CLEANER.isIn(stackTraceElement)) {
                        if (shouldSkip) {
                            shouldSkip = false;
                        } else {
                            return stackTraceElement;
                        }
                    }
                } catch (Exception e) {
                    // Fall back to slow path
                    break;
                }
                i++;
            }
        }

        // If we can't use the fast path of retrieving stackTraceElements, use the slow path by
        // iterating over the actual stacktrace
        for (StackTraceElement stackTraceElement : target.getStackTrace()) {
            if (CLEANER.isIn(stackTraceElement)) {
                if (shouldSkip) {
                    shouldSkip = false;
                } else {
                    return stackTraceElement;
                }
            }
        }
        return null;
    }

    /**
     * Finds the source file of the target stack trace.
     * Returns the default value if source file cannot be found.
     */
    public String findSourceFile(StackTraceElement[] target, String defaultValue) {
        for (StackTraceElement e : target) {
            if (CLEANER.isIn(e)) {
                return e.getFileName();
            }
        }
        return defaultValue;
    }
}
