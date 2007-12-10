package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.parents.MockitoException;

public class StubberTest extends RequiresValidState{

    private Stubber stubber;
    private MockitoStateImpl state;
    
    @Before
    public void setup() {
        state = new MockitoStateImpl();
        state.stubbingStarted();
        
        stubber = new Stubber(state);
        stubber.setInvocationForPotentialStubbing(new InvocationBuilder().toInvocationMatcher());
    }

    @Test
    public void shouldFinishStubbingBeforeValidatingThrowable() throws Exception {
        try {
            stubber.addThrowable(new Exception());
            fail();
        } catch (MockitoException e) {
            state.validateState();
        }
    }
    
    @Test
    public void shouldFinishStubbingOnAddingReturnValue() throws Exception {
        stubber.addReturnValue("test");
        state.validateState();
    }
    
    @Test
    public void shouldGetResultsForMethods() throws Throwable {
        Invocation simpleMethod = new InvocationBuilder().method("simpleMethod").toInvocation();
        stubber.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        stubber.addReturnValue("simpleMethod");
        
        Invocation differentMethod = new InvocationBuilder().method("differentMethod").toInvocation();
        stubber.setInvocationForPotentialStubbing(new InvocationMatcher(differentMethod));
        stubber.addThrowable(new IllegalStateException());
        
        assertEquals("simpleMethod", stubber.resultFor(simpleMethod));
        
        try {
            stubber.resultFor(differentMethod);
            fail();
        } catch (IllegalStateException e) {}
    }
    
    @Test
    public void shouldGetEmptyResultIfMethodsDontMatch() throws Throwable {
        Invocation simpleMethod = new InvocationBuilder().method("simpleMethod").toInvocation();
        stubber.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        stubber.addReturnValue("simpleMethod");
        
        Invocation differentMethod = new InvocationBuilder().method("differentMethod").toInvocation();
        
        assertEquals(null, stubber.resultFor(differentMethod));
    }
}