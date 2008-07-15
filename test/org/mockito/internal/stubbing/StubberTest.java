/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgressImpl;

public class StubberTest extends TestBase{

    private Stubber stubber;
    private MockingProgressImpl state;
    private Invocation simpleMethod;
    
    @Before
    public void setup() {
        state = new MockingProgressImpl();
        
        stubber = new Stubber(state);
        stubber.setInvocationForPotentialStubbing(new InvocationBuilder().toInvocationMatcher());
        
        simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
    }

    @Test
    public void shouldFinishStubbingWhenWrongThrowableIsSet() throws Exception {
        state.stubbingStarted();
        try {
            stubber.addAnswer(new ThrowsException(new Exception()));
            fail();
        } catch (MockitoException e) {
            state.validateState();
        }
    }
    
    @Test
    public void shouldFinishStubbingOnAddingReturnValue() throws Exception {
        state.stubbingStarted();
        stubber.addAnswer(new Returns("test"));
        state.validateState();
    }
    
    @Test
    public void shouldGetResultsForMethods() throws Throwable {
        stubber.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        stubber.addAnswer(new Returns("simpleMethod"));
        
        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        stubber.setInvocationForPotentialStubbing(new InvocationMatcher(differentMethod));
        stubber.addAnswer(new ThrowsException(new MyException()));
        
        assertEquals("simpleMethod", stubber.getResultFor(simpleMethod));
        
        try {
            stubber.getResultFor(differentMethod);
            fail();
        } catch (MyException e) {}
    }
    
    @Test
    public void shouldAddThrowableForVoidMethod() throws Throwable {
        stubber.addAnswerForVoidMethod(new ThrowsException(new MyException()));
        stubber.addVoidMethodForStubbing(new InvocationMatcher(simpleMethod));
        
        try {
            stubber.getResultFor(simpleMethod);
            fail();
        } catch (MyException e) {}
    }
    
    @Test
    public void shouldValidateThrowableForVoidMethod() throws Throwable {
        stubber.addAnswerForVoidMethod(new ThrowsException(new Exception()));
        
        try {
            stubber.addVoidMethodForStubbing(new InvocationMatcher(simpleMethod));
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldValidateThrowable() throws Throwable {
        try {
            stubber.addAnswer(new ThrowsException(null));
            fail();
        } catch (MockitoException e) {}
    }
    
    @SuppressWarnings("serial") class MyException extends RuntimeException {};
}