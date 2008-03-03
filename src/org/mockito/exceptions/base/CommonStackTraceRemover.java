/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Removes common part from exception an cause. It makes the exception stack trace really clean.
 */
public class CommonStackTraceRemover {

    public void remove(HasStackTrace hasStackTrace, List<StackTraceElement> causeStackTrace) {
        List<StackTraceElement> fullTrace = Arrays.asList(hasStackTrace.getStackTrace());
        List<StackTraceElement> cleanedPart = fullTrace;
        final int totalCount = fullTrace.size();
        for (int i = 0; i < totalCount; i++) {
            List<StackTraceElement> subList = fullTrace.subList(i, totalCount);
            int lastStartingIndexOfSubList = Collections.lastIndexOfSubList(causeStackTrace, subList);
            if (lastStartingIndexOfSubList == -1) {
                continue;
            }
            
            int lastEndingIndexOfSubList = lastStartingIndexOfSubList + subList.size() - 1;
            if (lastEndingIndexOfSubList == causeStackTrace.size() - 1) {
                cleanedPart = fullTrace.subList(0, i);
                break;
            }
        }
        
        hasStackTrace.setStackTrace(cleanedPart.toArray(new StackTraceElement[cleanedPart.size()]));
    }
}