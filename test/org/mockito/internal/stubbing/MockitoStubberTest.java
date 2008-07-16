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
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockitoutil.TestBase;

public class MockitoStubberTest extends TestBase{

    private MockitoStubber mockitoStubber;
    private MockingProgressImpl state;
    private Invocation simpleMethod;
    
    @Before
    public void setup() {
        state = new MockingProgressImpl();
        
        mockitoStubber = new MockitoStubber(state);
        mockitoStubber.setInvocationForPotentialStubbing(new InvocationBuilder().toInvocationMatcher());
        
        simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
    }

    @Test
    public void shouldFinishStubbingWhenWrongThrowableIsSet() throws Exception {
        state.stubbingStarted();
        try {
            mockitoStubber.addAnswer(new ThrowsException(new Exception()));
            fail();
        } catch (MockitoException e) {
            state.validateState();
        }
    }
    
    @Test
    public void shouldFinishStubbingOnAddingReturnValue() throws Exception {
        state.stubbingStarted();
        mockitoStubber.addAnswer(new Returns("test"));
        state.validateState();
    }
    
    @Test
    public void shouldGetResultsForMethods() throws Throwable {
        mockitoStubber.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        mockitoStubber.addAnswer(new Returns("simpleMethod"));
        
        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        mockitoStubber.setInvocationForPotentialStubbing(new InvocationMatcher(differentMethod));
        mockitoStubber.addAnswer(new ThrowsException(new MyException()));
        
        assertEquals("simpleMethod", mockitoStubber.getResultFor(simpleMethod));
        
        try {
            mockitoStubber.getResultFor(differentMethod);
            fail();
        } catch (MyException e) {}
    }
    
    @Test
    public void shouldAddThrowableForVoidMethod() throws Throwable {
        mockitoStubber.addAnswerForVoidMethod(new ThrowsException(new MyException()));
        mockitoStubber.addVoidMethodForStubbing(new InvocationMatcher(simpleMethod));
        
        try {
            mockitoStubber.getResultFor(simpleMethod);
            fail();
        } catch (MyException e) {}
    }
    
    @Test
    public void shouldValidateThrowableForVoidMethod() throws Throwable {
        mockitoStubber.addAnswerForVoidMethod(new ThrowsException(new Exception()));
        
        try {
            mockitoStubber.addVoidMethodForStubbing(new InvocationMatcher(simpleMethod));
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldValidateThrowable() throws Throwable {
        try {
            mockitoStubber.addAnswer(new ThrowsException(null));
            fail();
        } catch (MockitoException e) {}
    }
    
    @SuppressWarnings("serial") class MyException extends RuntimeException {};
}