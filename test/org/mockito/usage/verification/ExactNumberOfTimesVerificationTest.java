/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.NumberOfInvocationsAssertionError;

@SuppressWarnings("unchecked")
public class ExactNumberOfTimesVerificationTest {

    private LinkedList mock;
    
    @Before
    public void setup() {
        mock = Mockito.mock(LinkedList.class); 
    }
    
    @Test
    public void shouldVerifyActualNumberOfInvocationsSmallerThanExpected() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, 3).clear();
        try {
            Mockito.verify(mock, 100).clear();
            fail();
        } catch (NumberOfInvocationsAssertionError e) {
            String expected = 
                "\n" +
                "LinkedList.clear()" +
        		"\n" +
        		"Expected 100 times but was 3";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void shouldVerifyActualNumberOfInvocationsLargerThanExpected() throws Exception {
        mock.clear();
        mock.clear();
        mock.clear();

        Mockito.verify(mock, 3).clear();
        try {
            Mockito.verify(mock, 1).clear();
            fail();
        } catch (NumberOfInvocationsAssertionError e) {
            String expected = 
                "\n" +
                "LinkedList.clear()" +
                "\n" +
                "Expected 1 time but was 3";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void shouldVerifyProperlyIfMethodWasNotInvoked() throws Exception {
        Mockito.verify(mock, 0).clear();
        try {
            Mockito.verify(mock, 15).clear();
            fail();
        } catch (NumberOfInvocationsAssertionError e) {
            assertTrue(e.getMessage().endsWith("Expected 15 times but was 0"));
        }
    }
    
    @Test
    public void shouldVerifyProperlyIfMethodWasInvokedOnce() throws Exception {
        mock.clear();
        
        Mockito.verify(mock, 1).clear();
        try {
            Mockito.verify(mock, 15).clear();
            fail();
        } catch (NumberOfInvocationsAssertionError e) {
            assertTrue(e.getMessage().endsWith("Expected 15 times but was 1"));
        }
    }
    
    @Test
    public void shouldFailWhenExpectedNumberOfInvocationIsZero() throws Exception {
        mock.clear();
        
        try {
            Mockito.verify(mock, 0).clear();
            fail();
        } catch (NumberOfInvocationsAssertionError e) {}
    }
    
    @Test
    public void shouldVerifyWhenExpectedNumberOfInvocationIsZero() throws Exception {
        Mockito.verify(mock, 0).clear();
    }
    
    @Test
    public void shouldNotCountInStubbedInvocations() throws Exception {
        Mockito.stub(mock.add("test")).andReturn(false);
        Mockito.stub(mock.add("test")).andReturn(true);
        
        mock.add("test");
        mock.add("test");
        
        Mockito.verify(mock, 2).add("test");
    }
}
