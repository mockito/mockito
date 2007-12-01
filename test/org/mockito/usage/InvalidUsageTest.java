/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Strictly;
import org.mockito.exceptions.*;
import org.mockito.internal.StateResetter;

/**
 * invalid state happens if:
 *    -unfinished stubbing
 *    -unfinished stubVoid ?
 *    -stubbing without actual method call
 *    -verify without actual method call
 *
 * obviously we should consider if it is really important to cover all those naughty usage
 */
@SuppressWarnings("unchecked")
public class InvalidUsageTest {

    private IMethods mock;
    private IMethods mockTwo;

    @Before
    @After
    public void resetState() {
        StateResetter.reset();
        mock = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
    }
    
    @Test(expected=MockitoException.class)
    public void shouldRequireArgumentsWhenVerifyingNoMoreInteractions() {
        verifyNoMoreInteractions();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldRequireArgumentsWhenVerifyingZeroInteractions() {
        verifyZeroInteractions();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotCreateStrictlyWithoutMocks() {
        createStrictOrderVerifier();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotVerifyStrictlyUnfamilarMocks() {
        Strictly strictly = createStrictOrderVerifier(mock);
        strictly.verify(mockTwo).simpleMethod();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingInvalidCheckedException() throws Exception {
        stub(mock.simpleMethod()).andThrows(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullThrowable() throws Exception {
        stub(mock.simpleMethod()).andThrows(null);
    }    

    @Test(expected=MissingMethodInvocationException.class)
    public void shouldDetectStubbingWithoutMethodCallOnMock() {
        stub("blah".contains("blah"));
    }

    @Test
    public void shouldDetectUnfinishedStubbing() {
        stub(mock.simpleMethod());
        assertInvalidStateDetected(mock, UnfinishedStubbingException.class);
    }
    
    @Ignore
    @Test
    public void shouldDetectUnfinishedStubbingVoid() {
        stubVoid(mock);
        assertInvalidStateDetected(mock, UnfinishedStubbingException.class);
    }
    
    @Ignore
    @Test
    public void unfinishedStubbingVoid() {
        stubVoid(mock);

        try {
            mock.simpleMethod();
            fail();
        } catch (UnfinishedStubbingException e) {}
    }
    
    @Test
    public void shouldDetectUnfinishedVerification() {
        verify(mock);
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldDetectUnfinishedVerificationWhenVeryfingNoMoreInteractions() {
        verify(mock);
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldDetectUnfinishedVerificationWhenVeryfingZeroInteractions() {
        verify(mock);
        try {
            verifyZeroInteractions(mock);
            fail();
        } catch (MockitoException e) {}
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
            stub(mock.simpleMethod());
        }
    }
    
//    private static class OnStubVoid implements DetectsInvalidState {
//        public void detect(IMethods mock) {
//            stubVoid(mock);
//        }
//    }
    
    private static class OnMethodCallOnMock implements DetectsInvalidState {
        public void detect(IMethods mock) {
            mock.simpleMethod();
        }
    }
    
    private void assertInvalidStateDetected(IMethods mock, Class expected) {
        detects(new OnMethodCallOnMock(), mock, expected);
        detects(new OnStub(), mock, expected);
//        detects(new OnStubVoid(), mock, expected);
        detects(new OnVerify(), mock, expected);
        detects(new OnStrictVerify(), mock, expected);
        detects(new OnVerifyZeroInteractions(), mock, expected);
        detects(new OnVerifyNoMoreInteractions(), mock, expected);
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
