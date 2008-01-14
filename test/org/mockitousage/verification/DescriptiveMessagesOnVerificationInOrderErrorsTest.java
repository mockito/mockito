/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.RequiresValidState;
import org.mockito.InOrder;
import org.mockito.exceptions.cause.TooLittleInvocations;
import org.mockito.exceptions.cause.UndesiredInvocation;
import org.mockito.exceptions.cause.WantedDiffersFromActual;
import org.mockito.exceptions.verification.VerifcationInOrderFailed;
import org.mockitousage.IMethods;

public class DescriptiveMessagesOnVerificationInOrderErrorsTest extends RequiresValidState {
    
    private IMethods one;
    private IMethods two;
    private IMethods three;
    private InOrder inOrder;

    @Before
    public void setup() {
        one = Mockito.mock(IMethods.class);
        two = Mockito.mock(IMethods.class);
        three = Mockito.mock(IMethods.class);
        
        one.simpleMethod(1);
        one.simpleMethod(11);
        two.simpleMethod(2);
        two.simpleMethod(2);
        three.simpleMethod(3);
        
        inOrder = inOrder(one, two, three);
    }
    
    @Test
    public void shouldPrintVerificationInOrderErrorAndShowBothWantedAndActual() {
        inOrder.verify(one, atLeastOnce()).simpleMethod(1);
        
        try {
            inOrder.verify(one).simpleMethod(999);
            fail();
        } catch (VerifcationInOrderFailed e) {
            String expected = 
                    "\n" +
                    "Verification in order failed" +
                    "\n" +
                    "Wanted invocation:" +
                    "\n" +
                    "IMethods.simpleMethod(999)"; 
            
            assertEquals(expected, e.getMessage());
            
            assertEquals(e.getCause().getClass(), WantedDiffersFromActual.class);
            
            String expectedCause = 
                "\n" +
                "Actual invocation in order:" +
                "\n" +
                "IMethods.simpleMethod(11)";
            
            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }  
    
    @Test
    public void shouldPrintMethodThatWasNotInvoked() {
        inOrder.verify(one).simpleMethod(1);
        inOrder.verify(one).simpleMethod(11);
        inOrder.verify(two, times(2)).simpleMethod(2);
        inOrder.verify(three).simpleMethod(3);
        try {
            inOrder.verify(three).simpleMethod(999);
            fail();
        } catch (VerifcationInOrderFailed e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Verification in order failed" +
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
                    "IMethods.simpleMethod(999)"; 
            assertEquals(expectedMessage, actualMessage);     
        }
    }   
    
    @Test
    public void shouldPrintTooManyInvocations() {
        inOrder.verify(one).simpleMethod(1);
        inOrder.verify(one).simpleMethod(11);
        try {
            inOrder.verify(two, times(1)).simpleMethod(2);
            fail();
        } catch (VerifcationInOrderFailed e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Verification in order failed" +
                    "\n" +
                    "IMethods.simpleMethod(2)" +
                    "\n" +
                    "Wanted 1 time but was 2"; 
            assertEquals(expectedMessage, actualMessage);      
            
            assertEquals(UndesiredInvocation.class, e.getCause().getClass());

            String expectedCause =
                "\n" +
                "Undesired invocation:";
            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }  
    
    @Test
    public void shouldPrintTooLittleInvocations() {
        two.simpleMethod(2);
        
        inOrder.verify(one, atLeastOnce()).simpleMethod(anyInt());
        inOrder.verify(two, times(2)).simpleMethod(2);
        inOrder.verify(three, atLeastOnce()).simpleMethod(3);
        
        try {
            inOrder.verify(two, times(2)).simpleMethod(2);
            fail();
        } catch (VerifcationInOrderFailed e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Verification in order failed" +
                    "\n" +
                    "IMethods.simpleMethod(2)" +
                    "\n" +
                    "Wanted 2 times but was 1";
            assertEquals(expectedMessage, actualMessage);
            
            assertEquals(e.getCause().getClass(), TooLittleInvocations.class);
            
            String expectedCause = 
                "\n" +
                "Too little invocations:";
            
            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }   
}