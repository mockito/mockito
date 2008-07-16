/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import static org.mockitoutil.ExtraMatchers.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class StackTraceRemoverTest extends TestBase {

    private StackTraceElement[] stackTrace;
    private List<StackTraceElement> causeStackTrace;
    private List<String> methodsOnTraceAfterRemoving;
    
    private class SomeException implements HasStackTrace {
        public StackTraceElement[] getStackTrace() {
            return stackTrace;
        }
        public void setStackTrace(StackTraceElement[] stackTrace) {
            methodsOnTraceAfterRemoving = new LinkedList<String>();
            for(StackTraceElement e : stackTrace) {
                methodsOnTraceAfterRemoving.add(e.getMethodName());
            }
        }
    }
    
    private void remove() {
        CommonStackTraceRemover remover = new CommonStackTraceRemover();
        SomeException exception = new SomeException();
        remover.remove(exception, causeStackTrace);
    }
    
    private void setStackTrace(String ... methods) {
        stackTrace = new StackTraceBuilder().methods(methods).toStackTrace();
    }
    
    private void setCauseStackTrace(String ... methods) {
        causeStackTrace = new StackTraceBuilder().methods(methods).toStackTraceList();
    }
    
    @Test
    public void shouldRemoveCommonStackTrace() throws Exception {
        setStackTrace("methodOne", "methodTwo", "methodThree", "commonMethodOne", "commonMethodTwo");
        setCauseStackTrace("actualOne", "actualTwo", "commonMethodOne", "commonMethodTwo");
        remove();
        assertThat(methodsOnTraceAfterRemoving, hasExactlyInOrder(
                "methodOne",
                "methodTwo",
                "methodThree"
        ));
    }
    
    @Test
    public void shouldRemoveCommonWhenExceptionTraceHasRecursion() throws Exception {
        setStackTrace("methodOne", "commonMethodOne", "commonMethodTwo", "commonMethodOne", "commonMethodTwo");
        setCauseStackTrace("actualOne", "commonMethodOne", "commonMethodTwo");
        remove();
        assertThat(methodsOnTraceAfterRemoving, hasExactlyInOrder(
                "methodOne",
                "commonMethodOne",
                "commonMethodTwo"
        ));
    }

    @Test
    public void shouldRemoveCommonWhenActualTraceHasRecursion() throws Exception {
        setStackTrace("methodOne", "commonMethodOne");
        setCauseStackTrace("actualOne", "commonMethodOne", "actualOne", "commonMethodOne");
        remove();
        assertThat(methodsOnTraceAfterRemoving, hasExactlyInOrder(
                "methodOne"
        ));
    }
    
    @Test
    public void shouldRemoveCommonWhenOnlyOneMethodOnStackTrace() throws Exception {
        setStackTrace("commonOne");
        setCauseStackTrace("commonOne");
        remove();
        assertTrue(methodsOnTraceAfterRemoving.isEmpty());
    }
    
    @Test
    public void shouldRemoveCommonWhenOneMethodEach() throws Exception {
        setStackTrace("one", "commonOne");
        setCauseStackTrace("two", "commonOne");
        remove();
        assertThat(methodsOnTraceAfterRemoving, hasExactlyInOrder(
                "one"
        ));
    }
}