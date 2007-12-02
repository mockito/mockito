/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.exceptions.*;
import org.mockito.internal.StateResetter;

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
 *    -on verify on strictly
 *    -on stub
 *    -on stubVoid
 */
@SuppressWarnings("unchecked")
public class InvalidStateDetectionTest {

    private IMethods mock;

    @Before
    @After
    public void resetState() {
        StateResetter.reset();
        mock = mock(IMethods.class);
    }
    
    @Test
    public void shouldDetectUnfinishedStubbing() {
        stub(mock.simpleMethod());
        
        detects(new OnMethodCallOnMock(), mock, UnfinishedStubbingException.class);
        detects(new OnStub(), mock, UnfinishedStubbingException.class);
        detects(new OnStubVoid(), mock, UnfinishedStubbingException.class);
        detects(new OnVerify(), mock, UnfinishedStubbingException.class);
        detects(new OnStrictVerify(), mock, UnfinishedStubbingException.class);
        detects(new OnVerifyZeroInteractions(), mock, UnfinishedStubbingException.class);
        detects(new OnVerifyNoMoreInteractions(), mock, UnfinishedStubbingException.class);
    }
    
    @Test
    public void shouldDetectUnfinishedStubbingVoid() {
        stubVoid(mock);
        detects(new OnMethodCallOnMock(), mock, UnfinishedStubbingException.class);
        detects(new OnStub(), mock, UnfinishedStubbingException.class);
        detects(new OnStubVoid(), mock, UnfinishedStubbingException.class);
        detects(new OnVerify(), mock, UnfinishedStubbingException.class);
        detects(new OnStrictVerify(), mock, UnfinishedStubbingException.class);
        detects(new OnVerifyZeroInteractions(), mock, UnfinishedStubbingException.class);
        detects(new OnVerifyNoMoreInteractions(), mock, UnfinishedStubbingException.class);
    }
    
    @Test
    public void shouldDetectUnfinishedVerification() {
        verify(mock);
        detects(new OnStub(), mock, UnfinishedVerificationException.class);
        detects(new OnStubVoid(), mock, UnfinishedVerificationException.class);
        detects(new OnVerify(), mock, UnfinishedVerificationException.class);
        detects(new OnStrictVerify(), mock, UnfinishedVerificationException.class);
        detects(new OnVerifyZeroInteractions(), mock, UnfinishedVerificationException.class);
        detects(new OnVerifyNoMoreInteractions(), mock, UnfinishedVerificationException.class);
    }
    
    @Test
    @Ignore
    public void shouldCorrectStateAfterDetectingInvalidity() {
        
    }
    
    private static interface DetectsInvalidState {
        void detect(IMethods mock);
    }
    
    private static class OnVerify implements DetectsInvalidState {
        public void detect(IMethods mock) {
            verify(mock);
        }
    }
    
    private static class OnStrictVerify implements DetectsInvalidState {
        public void detect(IMethods mock) {
            createStrictOrderVerifier(mock).verify(mock);
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
    
    private static class OnStub implements DetectsInvalidState {
        public void detect(IMethods mock) {
            stub(mock.toString());
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
    
    private void detects(DetectsInvalidState detector, IMethods mock, Class expected) {
        try {
            detector.detect(mock);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals(expected, e.getClass());
        }
    }
}
