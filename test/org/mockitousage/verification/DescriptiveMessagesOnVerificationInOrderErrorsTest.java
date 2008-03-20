/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockito.exceptions.cause.TooLittleInvocations;
import org.mockito.exceptions.cause.UndesiredInvocation;
import org.mockito.exceptions.cause.WantedAnywhereAfterFollowingInteraction;
import org.mockito.exceptions.verification.ArgumentsAreDifferent;
import org.mockito.exceptions.verification.VerifcationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;

public class DescriptiveMessagesOnVerificationInOrderErrorsTest extends TestBase {
    
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
    public void shouldPrintVerificationInOrderErrorAndShowBothWantedAndPrevious() {
        inOrder.verify(one).simpleMethod(1);
        inOrder.verify(two, atLeastOnce()).simpleMethod(2);
        
        try {
            inOrder.verify(one, atLeastOnce()).simpleMethod(11);
            fail();
        } catch (VerifcationInOrderFailure e) {
            String expected = 
                    "\n" +
                    "Verification in order failure" +
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
                    "IMethods.simpleMethod(11);"; 
            
            assertEquals(expected, e.getMessage());
            
            assertEquals(e.getCause().getClass(), WantedAnywhereAfterFollowingInteraction.class);
            
            String expectedCause = 
                "\n" +
                "Wanted anywhere AFTER following interaction:" +
                "\n" +
                "IMethods.simpleMethod(2);";
            
            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }  
    
    @Test
    public void shouldPrintVerificationInOrderErrorAndShowWantedOnly() {
        try {
            inOrder.verify(one).differentMethod();
            fail();
        } catch (WantedButNotInvoked e) {
            String expected = 
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
                    "IMethods.differentMethod();"; 
            
            assertEquals(expected, e.getMessage());
            
            assertEquals(null, e.getCause());
        }
    } 
    
    @Ignore("i don't know how to implement it nicely... yet :)")
    @Test
    public void shouldPrintVerificationInOrderErrorAndShowWantedAndActual() {
        try {
            inOrder.verify(one).simpleMethod(999);
            fail();
        } catch (ArgumentsAreDifferent e) {
            String expected = 
                    "\n" +
                    "Arguments are different!" +
                    "\n" +
                    "IMethods.simpleMethod(999);"; 
            
            assertEquals(expected, e.getMessage());
            
            assertEquals(null, e.getCause());
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
        } catch (VerifcationInOrderFailure e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Verification in order failure" +
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
                    "IMethods.simpleMethod(999);"; 
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
        } catch (VerifcationInOrderFailure e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Verification in order failure" +
                    "\n" +
                    "IMethods.simpleMethod(2);" +
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
        } catch (VerifcationInOrderFailure e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Verification in order failure" +
                    "\n" +
                    "IMethods.simpleMethod(2);" +
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