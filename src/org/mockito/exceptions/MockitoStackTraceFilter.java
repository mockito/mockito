/*
 * Copyright (c) 2007, Szczepan Faber. 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import java.util.*;

public class MockitoStackTraceFilter {
    
    public boolean isLastStackElementToRemove(StackTraceElement e) {
        boolean firstOnStackIsMockitoClass = e.getClassName().equals("org.mockito.Mockito");
        boolean firstOnStackIsMockObject = e.getClassName().contains("$$EnhancerByCGLIB$$");
        return firstOnStackIsMockitoClass || firstOnStackIsMockObject;
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