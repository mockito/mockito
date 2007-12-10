package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.util.RequiresValidState;

public class StubberTest extends RequiresValidState{

    private Stubber s;
    
    @Before
    public void setup() {
        s = new Stubber();
        s.setInvocationForPotentialStubbing(new InvocationBuilder().toInvocationMatcher());
        MockitoState.instance().stubbingStarted();
    }

    @Test
    public void shouldFinishStubbingBeforeValidatingThrowable() throws Exception {
        try {
            s.addThrowable(new Exception());
            fail();
        } catch (MockitoException e) {
            MockitoState.instance().validateState();
        }
    }
    
    @Test
    public void shouldFinishStubbingOnAddingReturnValue() throws Exception {
        s.addReturnValue("test");
        MockitoState.instance().validateState();
    }
    
    @Test
    public void shouldGetResultsForMethods() throws Throwable {
        Invocation simpleMethod = new InvocationBuilder().method("simpleMethod").toInvocation();
        s.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        s.addReturnValue("simpleMethod");
        
        Invocation differentMethod = new InvocationBuilder().method("differentMethod").toInvocation();
        s.setInvocationForPotentialStubbing(new InvocationMatcher(differentMethod));
        s.addThrowable(new IllegalStateException());
        
        assertEquals("simpleMethod", s.resultFor(simpleMethod));
        
        try {
            s.resultFor(differentMethod);
            fail();
        } catch (IllegalStateException e) {}
    }
    
    @Test
    public void shouldGetEmptyResultIfMethodsDontMatch() throws Throwable {
        Invocation simpleMethod = new InvocationBuilder().method("simpleMethod").toInvocation();
        s.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        s.addReturnValue("simpleMethod");
        
        Invocation differentMethod = new InvocationBuilder().method("differentMethod").toInvocation();
        
        assertEquals(null, s.resultFor(differentMethod));
    }
}