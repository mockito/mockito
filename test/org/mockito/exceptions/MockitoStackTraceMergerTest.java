package org.mockito.exceptions;

import static org.junit.Assert.*;
import static org.mockito.util.ExtraMatchers.*;
import java.util.*;

import org.junit.Test;


public class MockitoStackTraceMergerTest {

    private StackTraceElement[] exceptionStackTrace;
    private List<StackTraceElement> actualInvocationStackTrace;
    private List<String> methodsOnTraceAfterMerging;
    
    private class SomeException implements HasStackTrace {
        public StackTraceElement[] getStackTrace() {
            return exceptionStackTrace;
        }
        public void setStackTrace(StackTraceElement[] stackTrace) {
            methodsOnTraceAfterMerging = new LinkedList<String>();
            for(StackTraceElement e : stackTrace) {
                methodsOnTraceAfterMerging.add(e.getMethodName());
            }
        }
    }
    
    private void merge() {
        MockitoStackTraceMerger merger = new MockitoStackTraceMerger();
        SomeException exception = new SomeException();
        merger.merge(exception, actualInvocationStackTrace);
    }
    
    private void exceptionStackTrace(String ... methods) {
        exceptionStackTrace = new StackTraceBuilder().methods(methods).toStackTrace();
    }
    
    private void actualInvocationStackTrace(String ... methods) {
        actualInvocationStackTrace = new StackTraceBuilder().methods(methods).toStackTraceList();
    }
    
    @Test
    public void shouldMergeStackTrace() throws Exception {
        exceptionStackTrace("methodOne", "methodTwo", "methodThree", "commonMethodOne", "commonMethodTwo");
        actualInvocationStackTrace("actualOne", "actualTwo", "commonMethodOne", "commonMethodTwo");
        merge();
        assertThat(methodsOnTraceAfterMerging, collectionContainingInOrder(
                "methodOne",
                "methodTwo",
                "methodThree",
                "_below_is_actual_invocation_stack_trace_",
                "actualOne",       
                "actualTwo",       
                "commonMethodOne", 
                "commonMethodTwo"
        ));
    }
    

    @Test
    public void shouldMergeWhenExceptionTraceHasRecursion() throws Exception {
        exceptionStackTrace("methodOne", "commonMethodOne", "commonMethodTwo", "commonMethodOne", "commonMethodTwo");
        actualInvocationStackTrace("actualOne", "commonMethodOne", "commonMethodTwo");
        merge();
        assertThat(methodsOnTraceAfterMerging, collectionContainingInOrder(
                "methodOne",
                "commonMethodOne",
                "commonMethodTwo",
                "_below_is_actual_invocation_stack_trace_",
                "actualOne",       
                "commonMethodOne", 
                "commonMethodTwo"
        ));
    }

    @Test
    public void shouldMergeWhenActualTraceHasRecursion() throws Exception {
        exceptionStackTrace("methodOne", "commonMethodOne");
        actualInvocationStackTrace("actualOne", "commonMethodOne", "actualOne", "commonMethodOne");
        merge();
        assertThat(methodsOnTraceAfterMerging, collectionContainingInOrder(
                "methodOne",
                "_below_is_actual_invocation_stack_trace_",
                "actualOne",       
                "commonMethodOne", 
                "actualOne",
                "commonMethodOne"
        ));
    }
    
    @Test
    public void shouldMergeWhenOnlyOneMethodOnStackTrace() throws Exception {
        exceptionStackTrace("commonOne");
        actualInvocationStackTrace("commonOne");
        merge();
        assertThat(methodsOnTraceAfterMerging, collectionContainingInOrder(
                "commonOne",
                "_below_is_actual_invocation_stack_trace_",
                "commonOne"       
        ));
    }
    
    @Test
    public void shouldMergeWhenOneMethodEach() throws Exception {
        exceptionStackTrace("one", "commonOne");
        actualInvocationStackTrace("two", "commonOne");
        merge();
        assertThat(methodsOnTraceAfterMerging, collectionContainingInOrder(
                "one",
                "_below_is_actual_invocation_stack_trace_",
                "two",
                "commonOne"
        ));
    }
}
