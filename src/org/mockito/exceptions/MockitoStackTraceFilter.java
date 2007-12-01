/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import java.util.*;

public class MockitoStackTraceFilter {
    
    public boolean isLastStackElementToRemove(StackTraceElement e) {
        boolean fromMockObject = e.getClassName().contains("$$EnhancerByCGLIB$$");
        boolean fromOrgMockito = e.getClassName().startsWith("org.mockito.");
        return fromMockObject || fromOrgMockito;
    }

    public void filterStackTrace(HasFilterableStackTrace hasFilterableStackTrace) {
        List<StackTraceElement> unfilteredStackTrace = Arrays.asList(hasFilterableStackTrace.getStackTrace());
        
        int lastToRemove = -1;
        int i = 0;
        for (StackTraceElement trace : unfilteredStackTrace) {
            if (this.isLastStackElementToRemove(trace)) {
                lastToRemove = i;
            }
            i++;
        }
        
        List<StackTraceElement> filtered = unfilteredStackTrace.subList(lastToRemove+1, unfilteredStackTrace.size() - 1);
        hasFilterableStackTrace.setStackTrace(filtered.toArray(new StackTraceElement[]{}));
    }
}