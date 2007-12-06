package org.mockito.exceptions;

import java.util.*;

public class MockitoStackTraceMerger {

    public void merge(HasStackTrace hasStackTrace, List<StackTraceElement> invocationStackTrace) {
        List<StackTraceElement> exceptionsPart = null;
        List<StackTraceElement> exceptionsTrace = Arrays.asList(hasStackTrace.getStackTrace());
        int length = exceptionsTrace.size();
        for(int i=0 ; i<length; i++) {
            List<StackTraceElement> subList = exceptionsTrace.subList(i, length);
            int lastStartingIndexOfSubList = Collections.lastIndexOfSubList(invocationStackTrace, subList);
            if (lastStartingIndexOfSubList == -1) {
                continue;
            }
            
            int lastEndingIndexOfSubList = lastStartingIndexOfSubList + subList.size() - 1;
            if (lastEndingIndexOfSubList == invocationStackTrace.size() - 1) {
                exceptionsPart = exceptionsTrace.subList(0, Math.max(i, 1));
                break;
            }
        }
        
        assert exceptionsPart != null;
        
        List<StackTraceElement> newStackTrace = new LinkedList<StackTraceElement>();
        newStackTrace.addAll(exceptionsPart);
        newStackTrace.add(new StackTraceElement("", "_below_is_actual_invocation_stack_trace_", "", 0));
        newStackTrace.addAll(invocationStackTrace);
        
//        int pointOfCommon = -1;
//        int index = 0;
//        for (StackTraceElement e : hasStackTrace.getStackTrace()) {
////            System.out.println(e);
////            System.err.println(invocationStackTrace.get(index));
//            if (pointOfCommon == -1 && invocationStackTrace.indexOf(e)) {
//                pointOfCommon = index;
//            }
//            
//            index++;
//        }
//        
//        assert pointOfCommon != -1;
        
//        List<StackTraceElement> newStackTrace = new LinkedList<StackTraceElement>();
//        List<StackTraceElement> exceptionTraceSublist = exceptionsTrace.subList(0, pointOfCommon);
//        newStackTrace.addAll(exceptionTraceSublist);
//        newStackTrace.add(new StackTraceElement("", "_below_is_actual_invocation_stack_trace_", "", 0));
//        newStackTrace.addAll(invocationStackTrace);
        
        hasStackTrace.setStackTrace(newStackTrace.toArray(new StackTraceElement[newStackTrace.size()]));
    }
}
