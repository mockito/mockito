/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.*;

@SuppressWarnings("unchecked")
public class BasicVerificationTest {

    private List mock;
    private List mockTwo;
    
    @Before 
    public void setup() {
        mock = Mockito.mock(List.class);
        mockTwo = Mockito.mock(List.class);
    }

    @Test
    public void shouldVerify() throws Exception {
        mock.clear();
        verify(mock).clear();

        mock.add("test");
        verify(mock).add("test");

        verifyNoMoreInteractions(mock);
    }

    @Test(expected=VerificationAssertionError.class)
    public void shouldFailVerification() throws Exception {
        verify(mock).clear();
    }

    @Test
    public void shouldFailVerificationOnMethodArgument() throws Exception {
        mock.clear();
        mock.add("foo");

        verify(mock).clear();
        try {
            verify(mock).add("bar");
            fail();
        } catch (VerificationAssertionError expected) {};
    }

    @Ignore
    @Test
    public void shouldLetVerifyTheSameMethodAnyTimes() throws Exception {
        mock.clear();
        mock.clear();
        
        mockTwo.add("add");

        verify(mock, atLeastOnce()).clear();
        verify(mockTwo, atLeastOnce()).add("add");
        try {
            verify(mockTwo, atLeastOnce()).add("foo");
            fail();
        } catch (VerificationAssertionError e) {}
    }

    @Test
    public void shouldDetectRedundantInvocation() throws Exception {
        mock.clear();
        mock.add("foo");
        mock.add("bar");

        verify(mock).clear();
        verify(mock).add("foo");

        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (VerificationAssertionError expected) {};
    }
    
    @Test
    public void shouldDetectWhenInvokedMoreThanOnce() throws Exception {
        mock.add("foo");
        mock.clear();
        mock.clear();
        
        verify(mock).add("foo");

        try {
            verify(mock).clear();
            fail();
        } catch (NumberOfInvocationsAssertionError e) {};
    }

    @Test
    public void shouldVerifyStubbedMethods() throws Exception {
        stub(mock.add("test")).andReturn(Boolean.FALSE);
        
        mock.add("test");
        
        verify(mock).add("test");
    }
}