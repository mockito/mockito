/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockitoutil.TestBase;

public class MockitoStubberTest extends TestBase{

    private InvocationContainerImpl invocationContainerImpl;
    private MockingProgress state;
    private Invocation simpleMethod;

    @Before
    public void setup() {
        state = new MockingProgressImpl();
        
        invocationContainerImpl = new InvocationContainerImpl(state);
        invocationContainerImpl.setInvocationForPotentialStubbing(new InvocationBuilder().toInvocationMatcher());
        
        simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
    }

    @Test
    public void shouldFinishStubbingWhenWrongThrowableIsSet() throws Exception {
        state.stubbingStarted();
        try {
            invocationContainerImpl.addAnswer(new ThrowsException(new Exception()));
            fail();
        } catch (MockitoException e) {
            state.validateState();
        }
    }
    
    @Test
    public void shouldFinishStubbingOnAddingReturnValue() throws Exception {
        state.stubbingStarted();
        invocationContainerImpl.addAnswer(new Returns("test"));
        state.validateState();
    }
    
    @Test
    public void shouldGetResultsForMethods() throws Throwable {
        invocationContainerImpl.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        invocationContainerImpl.addAnswer(new Returns("simpleMethod"));
        
        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        invocationContainerImpl.setInvocationForPotentialStubbing(new InvocationMatcher(differentMethod));
        invocationContainerImpl.addAnswer(new ThrowsException(new MyException()));
        
        assertEquals("simpleMethod", invocationContainerImpl.answerTo(simpleMethod));
        
        try {
            invocationContainerImpl.answerTo(differentMethod);
            fail();
        } catch (MyException e) {}
    }
    
    @Test
    public void shouldAddThrowableForVoidMethod() throws Throwable {
        invocationContainerImpl.addAnswerForVoidMethod(new ThrowsException(new MyException()));
        invocationContainerImpl.setMethodForStubbing(new InvocationMatcher(simpleMethod));
        
        try {
            invocationContainerImpl.answerTo(simpleMethod);
            fail();
        } catch (MyException e) {}
    }
    
    @Test
    public void shouldValidateThrowableForVoidMethod() throws Throwable {
        invocationContainerImpl.addAnswerForVoidMethod(new ThrowsException(new Exception()));
        
        try {
            invocationContainerImpl.setMethodForStubbing(new InvocationMatcher(simpleMethod));
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldValidateThrowable() throws Throwable {
        try {
            invocationContainerImpl.addAnswer(new ThrowsException(null));
            fail();
        } catch (MockitoException e) {}
    }
    
    @SuppressWarnings("serial") 
    class MyException extends RuntimeException {};
}