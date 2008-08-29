/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.StateMaster;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

/**
 * invalid state happens if:
 * 
 *    -unfinished stubbing
 *    -unfinished stubVoid
 *    -stubbing without actual method call
 *    -verify without actual method call
 *    
 * we should aim to detect invalid state in following scenarios:
 * 
 *    -on method call on mock
 *    -on verify
 *    -on verifyZeroInteractions
 *    -on verifyNoMoreInteractions
 *    -on verify in order
 *    -on stub
 *    -on stubVoid
 */
@SuppressWarnings({"unchecked", "deprecation"})
public class InvalidStateDetectionTest extends TestBase {

    private IMethods mock;

    @Before
    @After
    public void resetState() {
        StateMaster.reset();
        mock = mock(IMethods.class);
    }
    
    @Test
    public void shouldDetectUnfinishedStubbing() {
        when(mock.simpleMethod());
        detects(new OnMethodCallOnMock(), UnfinishedStubbingException.class);

        when(mock.simpleMethod());
        detects(new OnStub(), UnfinishedStubbingException.class);
        
        when(mock.simpleMethod());
        detects(new OnStubVoid(), UnfinishedStubbingException.class);
        
        when(mock.simpleMethod());
        detects(new OnVerify(), UnfinishedStubbingException.class);
        
        when(mock.simpleMethod());
        detects(new OnVerifyInOrder(), UnfinishedStubbingException.class);
        
        when(mock.simpleMethod());
        detects(new OnVerifyZeroInteractions(), UnfinishedStubbingException.class);
        
        when(mock.simpleMethod());
        detects(new OnVerifyNoMoreInteractions(), UnfinishedStubbingException.class);

        when(mock.simpleMethod());
        detects(new OnDoAnswer(), UnfinishedStubbingException.class);
    }
    
    @Test
    public void shouldDetectUnfinishedStubbingVoid() {
        stubVoid(mock);
        detects(new OnMethodCallOnMock(), UnfinishedStubbingException.class);
        
        stubVoid(mock);
        detects(new OnStub(), UnfinishedStubbingException.class);
        
        stubVoid(mock);
        detects(new OnStubVoid(), UnfinishedStubbingException.class);
        
        stubVoid(mock);
        detects(new OnVerify(), UnfinishedStubbingException.class);
        
        stubVoid(mock);
        detects(new OnVerifyInOrder(), UnfinishedStubbingException.class);
        
        stubVoid(mock);
        detects(new OnVerifyZeroInteractions(), UnfinishedStubbingException.class);
        
        stubVoid(mock);
        detects(new OnVerifyNoMoreInteractions(), UnfinishedStubbingException.class);
        
        stubVoid(mock);
        detects(new OnDoAnswer(), UnfinishedStubbingException.class);
    }
    
    @Test
    public void shouldDetectUnfinishedDoAnswerStubbing() {
        doAnswer(null);
        detects(new OnMethodCallOnMock(), UnfinishedStubbingException.class);
        
        doAnswer(null);
        detects(new OnStub(), UnfinishedStubbingException.class);
        
        doAnswer(null);
        detects(new OnStubVoid(), UnfinishedStubbingException.class);
        
        doAnswer(null);
        detects(new OnVerify(), UnfinishedStubbingException.class);
        
        doAnswer(null);
        detects(new OnVerifyInOrder(), UnfinishedStubbingException.class);
        
        doAnswer(null);
        detects(new OnVerifyZeroInteractions(), UnfinishedStubbingException.class);
        
        doAnswer(null);
        detects(new OnVerifyNoMoreInteractions(), UnfinishedStubbingException.class);
        
        doAnswer(null);
        detects(new OnDoAnswer(), UnfinishedStubbingException.class);
    }
    
    @Test
    public void shouldDetectUnfinishedVerification() {
        verify(mock);
        detects(new OnStub(), UnfinishedVerificationException.class);
        
        verify(mock);
        detects(new OnStubVoid(), UnfinishedVerificationException.class);
        
        verify(mock);
        detects(new OnVerify(), UnfinishedVerificationException.class);
        
        verify(mock);
        detects(new OnVerifyInOrder(), UnfinishedVerificationException.class);
        
        verify(mock);
        detects(new OnVerifyZeroInteractions(), UnfinishedVerificationException.class);
        
        verify(mock);
        detects(new OnVerifyNoMoreInteractions(), UnfinishedVerificationException.class);
        
        verify(mock);
        detects(new OnDoAnswer(), UnfinishedVerificationException.class);
    }
    
    @Test
    public void shouldCorrectStateAfterDetectingUnfinishedStubbing() {
        stubVoid(mock).toThrow(new RuntimeException());
        
        try {
            stubVoid(mock).toThrow(new RuntimeException()).on().oneArg(true);
            fail();
        } catch (UnfinishedStubbingException e) {}
        
        stubVoid(mock).toThrow(new RuntimeException()).on().oneArg(true);
        try {
            mock.oneArg(true);
            fail();
        } catch (RuntimeException e) {}
    }
    
    @Test
    public void shouldCorrectStateAfterDetectingUnfinishedVerification() {
        mock.simpleMethod();
        verify(mock);
        
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (UnfinishedVerificationException e) {}
        
        verify(mock).simpleMethod();
    }
    
    private static interface DetectsInvalidState {
        void detect(IMethods mock);
    }
    
    private static class OnVerify implements DetectsInvalidState {
        public void detect(IMethods mock) {
            verify(mock);
        }
    }
    
    private static class OnVerifyInOrder implements DetectsInvalidState {
        public void detect(IMethods mock) {
            inOrder(mock).verify(mock);
        }
    }
    
    private static class OnVerifyZeroInteractions implements DetectsInvalidState {
        public void detect(IMethods mock) {
            verifyZeroInteractions(mock);
        }
    }
    
    private static class OnVerifyNoMoreInteractions implements DetectsInvalidState {
        public void detect(IMethods mock) {
            verifyNoMoreInteractions(mock);
        }
    }   
    
    private static class OnDoAnswer implements DetectsInvalidState {
        public void detect(IMethods mock) {
            doAnswer(null);
        }
    }  
    
    private static class OnStub implements DetectsInvalidState {
        public void detect(IMethods mock) {
            when(mock);
        }
    }
    
    private static class OnStubVoid implements DetectsInvalidState {
        public void detect(IMethods mock) {
            stubVoid(mock);
        }
    }
    
    private static class OnMethodCallOnMock implements DetectsInvalidState {
        public void detect(IMethods mock) {
            mock.simpleMethod();
        }
    }
    
    private void detects(DetectsInvalidState detector, Class expected) {
        try {
            detector.detect(mock);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals(expected, e.getClass());
        }
    }
}
