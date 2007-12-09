package org.mockito.exceptions.parents;

import static org.junit.Assert.assertThat;
import static org.mockito.util.ExtraMatchers.collectionIsExactlyInOrder;

import java.util.*;

import org.junit.Test;
import org.mockito.exceptions.parents.CommonStackTraceRemover;
import org.mockito.util.RequiresValidState;

public class StackTraceRemoverTest extends RequiresValidState {

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
        assertThat(methodsOnTraceAfterRemoving, collectionIsExactlyInOrder(
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
        assertThat(methodsOnTraceAfterRemoving, collectionIsExactlyInOrder(
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
        assertThat(methodsOnTraceAfterRemoving, collectionIsExactlyInOrder(
                "methodOne"
        ));
    }
    
    @Test
    public void shouldRemoveCommonWhenOnlyOneMethodOnStackTrace() throws Exception {
        setStackTrace("commonOne");
        setCauseStackTrace("commonOne");
        remove();
        assertThat(methodsOnTraceAfterRemoving, collectionIsExactlyInOrder());
    }
    
    @Test
    public void shouldRemoveCommonWhenOneMethodEach() throws Exception {
        setStackTrace("one", "commonOne");
        setCauseStackTrace("two", "commonOne");
        remove();
        assertThat(methodsOnTraceAfterRemoving, collectionIsExactlyInOrder(
                "one"
        ));
    }
}