/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import java.util.Arrays;
import java.util.List;

public class StackTraceFilter {
    
    public boolean isLastStackElementToRemove(StackTraceElement e) {
        boolean fromMockObject = e.getClassName().contains("$$EnhancerByMockitoWithCGLIB$$");
        boolean fromOrgMockito = e.getClassName().startsWith("org.mockito.");
        boolean isRunner = e.getClassName().startsWith("org.mockito.runners.");
        return fromMockObject || fromOrgMockito && !isRunner;
    }

    public void filterStackTrace(HasStackTrace hasStackTrace) {
        List<StackTraceElement> unfilteredStackTrace = Arrays.asList(hasStackTrace.getStackTrace());
        
        int lastToRemove = -1;
        int i = 0;
        for (StackTraceElement trace : unfilteredStackTrace) {
            if (this.isLastStackElementToRemove(trace)) {
                lastToRemove = i;
            }
            i++;
        }
        
        List<StackTraceElement> filtered = unfilteredStackTrace.subList(lastToRemove + 1, unfilteredStackTrace.size());
        hasStackTrace.setStackTrace(filtered.toArray(new StackTraceElement[]{}));
    }
}