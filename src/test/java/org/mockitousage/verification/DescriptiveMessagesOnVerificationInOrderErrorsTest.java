/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

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
        } catch (VerificationInOrderFailure e) {
            String expected = 
                    "\n" +
                    "Verification in order failure" +
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
                    "iMethods.simpleMethod(11);" +
                    "\n" +
                    "-> at "; 
            
            assertContains(expected, e.getMessage());
            
            String expectedCause = 
                "\n" +
                "Wanted anywhere AFTER following interaction:" +
                "\n" +
                "iMethods.simpleMethod(2);" +
                "\n" +
                "-> at ";
            
            assertContains(expectedCause, e.getMessage());
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
                    "iMethods.differentMethod();" +
                    "\n" +
                    "-> at"; 
            
            assertContains(expected, e.getMessage());
        }
    } 
    
    @Test
    public void shouldPrintVerificationInOrderErrorAndShowWantedAndActual() {
        try {
            inOrder.verify(one).simpleMethod(999);
            fail();
        } catch (org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {           
            assertContains("has different arguments", e.getMessage());
        }
    }
    
    @Test
    public void shouldNotSayArgumentsAreDifferent() {
        //this is the last invocation so any next verification in order should simply say wanted but not invoked
        inOrder.verify(three).simpleMethod(3);
        try {
            inOrder.verify(one).simpleMethod(999);
            fail();
        } catch (VerificationInOrderFailure e) {
            assertContains("Wanted but not invoked", e.getMessage());
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
        } catch (VerificationInOrderFailure e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Verification in order failure" +
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
                    "iMethods.simpleMethod(999);"; 
            assertContains(expectedMessage, actualMessage);     
        }
    }   
    
    @Test
    public void shouldPrintTooManyInvocations() {
        inOrder.verify(one).simpleMethod(1);
        inOrder.verify(one).simpleMethod(11);
        try {
            inOrder.verify(two, times(1)).simpleMethod(2);
            fail();
        } catch (VerificationInOrderFailure e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Verification in order failure:" +
                    "\n" +
                    "iMethods.simpleMethod(2);" +
                    "\n" +
                    "Wanted 1 time:" +
                    "\n" +
                    "-> at"; 
            assertContains(expectedMessage, actualMessage);      

            String expectedCause =
                "\n" +
                "But was 2 times. Undesired invocation:" +
                "\n" +
                "-> at";
            assertContains(expectedCause, e.getMessage());
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
        } catch (VerificationInOrderFailure e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Verification in order failure:" +
                    "\n" +
                    "iMethods.simpleMethod(2);" +
                    "\n" +
                    "Wanted 2 times:" +
                    "\n" +
                    "-> at";
            assertContains(expectedMessage, actualMessage);
            
            String expectedCause = 
                "\n" +
                "But was 1 time:" +
                "\n" +
                "-> at";
            
            assertContains(expectedCause, e.getMessage());
        }
    }   
}