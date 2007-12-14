/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.util.ExtraMatchers.messageContains;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.cause.FirstUndesiredInvocation;
import org.mockito.exceptions.cause.TooLittleInvocations;
import org.mockito.exceptions.verification.TooLittleActualInvocationsError;
import org.mockito.exceptions.verification.TooManyActualInvocationsError;

@SuppressWarnings("unchecked")
public class ExactNumberOfTimesVerificationTest extends RequiresValidState {

    private LinkedList mock;
    
    @Before
    public void setup() {
        mock = Mockito.mock(LinkedList.class); 
    }
    
    @Test
    public void shouldVerifyActualNumberOfInvocationsSmallerThanWanted() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, times(3)).clear();
        try {
            Mockito.verify(mock, times(100)).clear();
            fail();
        } catch (TooLittleActualInvocationsError e) {
            String expected = 
                "\n" +
                "LinkedList.clear()" +
        		"\n" +
        		"Wanted 100 times but was 3";
            assertEquals(expected, e.getMessage());
            
            assertEquals(TooLittleInvocations.class, e.getCause().getClass());
            
            String expectedCause = 
                "\n" +
                "Too little invocations:";
            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }
    
    @Test
    public void shouldVerifyActualNumberOfInvocationsLargerThanWanted() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, times(4)).clear();
        try {
            Mockito.verify(mock, times(1)).clear();
            fail();
        } catch (TooManyActualInvocationsError e) {
            String expected = 
                "\n" +
                "LinkedList.clear()" +
                "\n" +
                "Wanted 1 time but was 4";
            assertEquals(expected, e.getMessage());
            
            assertEquals(FirstUndesiredInvocation.class, e.getCause().getClass());
            
            String expectedCause = 
                "\n" +
                "First undesired invocation:";
            
            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }
    
    @Test
    public void shouldVerifyProperlyIfMethodWasNotInvoked() throws Exception {
        Mockito.verify(mock, times(0)).clear();
        try {
            Mockito.verify(mock, times(15)).clear();
            fail();
        } catch (TooLittleActualInvocationsError e) {
            assertThat(e, messageContains("Wanted 15 times but was 0"));
        }
    }
    
    @Test
    public void shouldVerifyProperlyIfMethodWasInvokedOnce() throws Exception {
        mock.clear();
        
        Mockito.verify(mock, times(1)).clear();
        try {
            Mockito.verify(mock, times(15)).clear();
            fail();
        } catch (TooLittleActualInvocationsError e) {
            assertThat(e, messageContains("Wanted 15 times but was 1"));
        }
    }
    
    @Test
    public void shouldFailWhenWantedNumberOfInvocationIsZero() throws Exception {
        mock.clear();
        
        try {
            Mockito.verify(mock, times(0)).clear();
            fail();
        } catch (TooManyActualInvocationsError e) {
            assertThat(e, messageContains("Wanted 0 times but was 1"));
        }
    }
    
    @Test
    public void shouldVerifyWhenWantedNumberOfInvocationIsZero() throws Exception {
        Mockito.verify(mock, times(0)).clear();
    }
    
    @Test
    public void shouldNotCountInStubbedInvocations() throws Exception {
        Mockito.stub(mock.add("test")).andReturn(false);
        Mockito.stub(mock.add("test")).andReturn(true);
        
        mock.add("test");
        mock.add("test");
        
        Mockito.verify(mock, times(2)).add("test");
    }
}
