/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.verification;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.VerificationError;
import org.mockito.usage.IMethods;

public class NiceMessagesOnStrictOrderErrorsTest {
    
    private IMethods one;
    private IMethods two;
    private IMethods three;
    private Strictly strictly;

    @Before
    public void setup() {
        one = Mockito.mock(IMethods.class);
        two = Mockito.mock(IMethods.class);
        three = Mockito.mock(IMethods.class);
        
        one.simpleMethod(1);
        one.simpleMethod(11);
        two.simpleMethod(2);
        two.simpleMethod(2);
        three.simpleMethod();
        
        strictly = createStrictOrderVerifier(one, two, three);
    }
    
    @Test
    public void shouldPrintStrictVerificationError() {
        try {
            strictly.verify(one).simpleMethod(999);
            fail();
        } catch (VerificationError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Strict order verification failed" +
                    "\n" +
                    "Wanted: IMethods.simpleMethod(999)" + 
            		"\n" +
            		"Actual: IMethods.simpleMethod(1)";
            assertEquals(expectedMessage, actualMessage);         
        }
    }  
    
    @Test
    public void shouldPrintWantedMethodWhenEverythingElseIsVerified() {
        strictly.verify(one).simpleMethod(1);
        strictly.verify(one).simpleMethod(11);
        strictly.verify(two, 2).simpleMethod(2);
        strictly.verify(three).simpleMethod();
        try {
            strictly.verify(three).simpleMethod(999);
            fail();
        } catch (VerificationError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
                    "IMethods.simpleMethod(999)"; 
            assertEquals(expectedMessage, actualMessage);         
        }
    }   
}