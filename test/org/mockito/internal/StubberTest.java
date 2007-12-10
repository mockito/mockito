package org.mockito.internal;

import org.junit.Test;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.util.RequiresValidState;

import static org.junit.Assert.*;

public class StubberTest extends RequiresValidState{

    @Test
    public void shouldFinishStubbingBeforeValidatingThrowable() throws Exception {
        Stubber s = new Stubber();
        s.setInvocationForPotentialStubbing(new InvocationBuilder().toMatchingInvocation());
        
        MockitoState.instance().stubbingStarted();
        try {
            s.addThrowable(new Exception());
            fail();
        } catch (MockitoException e) {
            MockitoState.instance().validateState();
        }
    }
    
    @Test
    public void shouldFinishStubbingOnAddingReturnValue() throws Exception {
        Stubber s = new Stubber();
        s.setInvocationForPotentialStubbing(new InvocationBuilder().toMatchingInvocation());
        
        MockitoState.instance().stubbingStarted();
        s.addReturnValue("test");
        MockitoState.instance().validateState();
    }
}