/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.junit.Assert.*;

import java.nio.charset.CharacterCodingException;

import org.junit.*;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockito.internal.stubbing.Stubber;

public class StubberTest extends RequiresValidState{

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
    public void shouldFinishStubbingBeforeValidatingThrowable() throws Exception {
        state.stubbingStarted();
        try {
            stubber.addThrowable(new Exception());
            fail();
        } catch (MockitoException e) {
            state.validateState();
        }
    }
    
    @Test
    public void shouldFinishStubbingOnAddingReturnValue() throws Exception {
        state.stubbingStarted();
        stubber.addReturnValue("test");
        state.validateState();
    }
    
    @Test
    public void shouldGetResultsForMethods() throws Throwable {
        stubber.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        stubber.addReturnValue("simpleMethod");
        
        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        stubber.setInvocationForPotentialStubbing(new InvocationMatcher(differentMethod));
        stubber.addThrowable(new MyException());
        
        assertEquals("simpleMethod", stubber.resultFor(simpleMethod));
        
        try {
            stubber.resultFor(differentMethod);
            fail();
        } catch (MyException e) {}
    }
    
    @Test
    public void shouldGetEmptyResultIfMethodsDontMatch() throws Throwable {
        stubber.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        stubber.addReturnValue("simpleMethod");
        
        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        
        assertEquals(null, stubber.resultFor(differentMethod));
    }
    
    @Test
    public void shouldAddThrowableForVoidMethod() throws Throwable {
        stubber.addThrowableForVoidMethod(new MyException());
        stubber.addVoidMethodForThrowable(new InvocationMatcher(simpleMethod));
        
        try {
            stubber.resultFor(simpleMethod);
            fail();
        } catch (MyException e) {};
    }
    
    @Test
    public void shouldValidateThrowableForVoidMethod() throws Throwable {
        stubber.addThrowableForVoidMethod(new Exception());
        
        try {
            stubber.addVoidMethodForThrowable(new InvocationMatcher(simpleMethod));
            fail();
        } catch (MockitoException e) {};
    }
    
    @Test
    public void shouldValidateNullThrowable() throws Throwable {
        try {
            stubber.addThrowable(null);
            fail();
        } catch (MockitoException e) {};
    }
    
    @Test
    public void shouldLetSettingProperCheckedException() throws Throwable {
        stubber.setInvocationForPotentialStubbing(new InvocationBuilder().method("canThrowException").toInvocationMatcher());
        stubber.addThrowable(new CharacterCodingException());
    }
    
    @SuppressWarnings("serial") class MyException extends RuntimeException {};
}