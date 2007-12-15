/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.verification.TooManyActualInvocationsError;
import org.mockito.exceptions.verification.VerificationError;

@SuppressWarnings("unchecked")
public class BasicVerificationTest extends RequiresValidState {

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

    @Test(expected=VerificationError.class)
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
        } catch (VerificationError expected) {}
    }

    @Test
    public void shouldFailOnWrongMethod() throws Exception {
        mock.clear();
        mock.clear();
        
        mockTwo.add("add");

        verify(mock, atLeastOnce()).clear();
        verify(mockTwo, atLeastOnce()).add("add");
        try {
            verify(mockTwo, atLeastOnce()).add("foo");
            fail();
        } catch (VerificationError e) {}
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
        } catch (VerificationError expected) {}
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
        } catch (TooManyActualInvocationsError e) {}
    }

    @Test
    public void shouldVerifyStubbedMethods() throws Exception {
        stub(mock.add("test")).andReturn(Boolean.FALSE);
        
        mock.add("test");
        
        verify(mock).add("test");
    }
}