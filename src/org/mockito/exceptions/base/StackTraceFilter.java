/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import java.util.Arrays;
import java.util.List;

import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.internal.configuration.GlobalConfiguration;

public class StackTraceFilter {
    
    private IMockitoConfiguration config = new GlobalConfiguration();
    
    public boolean isLastStackElementToRemove(StackTraceElement e) {
        boolean fromMockObject = e.getClassName().contains("$$EnhancerByMockitoWithCGLIB$$");
        boolean fromOrgMockito = e.getClassName().startsWith("org.mockito.");
        boolean isRunner = e.getClassName().startsWith("org.mockito.runners.");
        return fromMockObject || fromOrgMockito && !isRunner;
    }

    public void filterConditionally(Throwable throwable) {
        if (!config.cleansStackTrace()) {
            return;
        }
        StackTraceElement[] filtered = filter(throwable.getStackTrace());
        throwable.setStackTrace(filtered);
    }

    public StackTraceElement[] filter(StackTraceElement[] target) {
        List<StackTraceElement> unfilteredStackTrace = Arrays.asList(target);
        
        int lastToRemove = -1;
        int i = 0;
        for (StackTraceElement trace : unfilteredStackTrace) {
            if (this.isLastStackElementToRemove(trace)) {
                lastToRemove = i;
            }
            i++;
        }
        
        List<StackTraceElement> filtered = unfilteredStackTrace.subList(lastToRemove + 1, unfilteredStackTrace.size());
        return filtered.toArray(new StackTraceElement[]{});
    }
}