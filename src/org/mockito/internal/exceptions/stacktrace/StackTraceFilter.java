/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.exceptions.stacktrace;

import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.internal.configuration.ClassPathLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StackTraceFilter implements Serializable {

    static final long serialVersionUID = -5499819791513105700L;

    private static final StackTraceCleaner CLEANER =
            ClassPathLoader.getStackTraceCleanerProvider().getStackTraceCleaner(new DefaultStackTraceCleaner());
    
    /**
     * Example how the filter works (+/- means good/bad):
     * [a+, b+, c-, d+, e+, f-, g+] -> [a+, b+, g+]
     * Basically removes all bad from the middle. If any good are in the middle of bad those are also removed. 
     */
    public StackTraceElement[] filter(StackTraceElement[] target, boolean keepTop) {
        //TODO: profile
        List<StackTraceElement> unfilteredStackTrace = Arrays.asList(target);
        
        int lastBad = -1;
        int firstBad = -1;
        for (int i = 0; i < unfilteredStackTrace.size(); i++) {
            if (!CLEANER.isOut(unfilteredStackTrace.get(i))) {
                continue;
            }
            lastBad = i;
            if (firstBad == -1) {
                firstBad = i;
            }
        }
        
        List<StackTraceElement> top;
        if (keepTop && firstBad != -1) {
            top = unfilteredStackTrace.subList(0, firstBad);
        } else {
            top = new LinkedList<StackTraceElement>();
        }
        
        List<StackTraceElement> bottom = unfilteredStackTrace.subList(lastBad + 1, unfilteredStackTrace.size());
        List<StackTraceElement> filtered = new ArrayList<StackTraceElement>(top);
        filtered.addAll(bottom);
        return filtered.toArray(new StackTraceElement[]{});
    }
}