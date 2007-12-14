/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.parents;

import java.util.*;


public class CommonStackTraceRemover {

    public void remove(HasStackTrace hasStackTrace, List<StackTraceElement> causeStackTrace) {
        List<StackTraceElement> exceptionsPart = null;
        List<StackTraceElement> exceptionsTrace = Arrays.asList(hasStackTrace.getStackTrace());
        int length = exceptionsTrace.size();
        for(int i=0 ; i<length; i++) {
            List<StackTraceElement> subList = exceptionsTrace.subList(i, length);
            int lastStartingIndexOfSubList = Collections.lastIndexOfSubList(causeStackTrace, subList);
            if (lastStartingIndexOfSubList == -1) {
                continue;
            }
            
            int lastEndingIndexOfSubList = lastStartingIndexOfSubList + subList.size() - 1;
            if (lastEndingIndexOfSubList == causeStackTrace.size() - 1) {
                exceptionsPart = exceptionsTrace.subList(0, i);
                break;
            }
        }
        
        assert exceptionsPart != null;
        hasStackTrace.setStackTrace(exceptionsPart.toArray(new StackTraceElement[exceptionsPart.size()]));
    }
}
