/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.exceptions.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.internal.configuration.plugins.Plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StackTraceFilter implements Serializable {

    static final long serialVersionUID = -5499819791513105700L;

    private static final StackTraceCleaner CLEANER =
            Plugins.getStackTraceCleanerProvider().getStackTraceCleaner(new DefaultStackTraceCleaner());

    /**
     * Example how the filter works (+/- means good/bad):
     * [a+, b+, c-, d+, e+, f-, g+] -> [a+, b+, d+, e+, g+]
     * Basically removes all bad from the middle.
     * <strike>If any good are in the middle of bad those are also removed.</strike>
     */
    public StackTraceElement[] filter(StackTraceElement[] target, boolean keepTop) {
        //TODO: profile
        final List<StackTraceElement> filtered = new ArrayList<StackTraceElement>();
        for (StackTraceElement aTarget : target) {
            if (!CLEANER.isOut(aTarget)) {
                filtered.add(aTarget);
            }
        }
        StackTraceElement[] result = new StackTraceElement[filtered.size()];
        return filtered.toArray(result);
    }
}