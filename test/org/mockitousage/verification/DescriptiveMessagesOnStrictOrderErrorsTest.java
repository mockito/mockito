/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;
import static org.mockito.util.ExtraMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.cause.WantedDiffersFromActual;
import org.mockito.exceptions.verification.*;
import org.mockitousage.IMethods;

public class DescriptiveMessagesOnStrictOrderErrorsTest extends RequiresValidState {
    
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
        } catch (VerificationError e) {
            String expected = 
                    "\n" +
                    "Invocation differs from actual" +
                    "\n" +
                    "Wanted invocation:" +
                    "\n" +
                    "IMethods.simpleMethod(999)"; 
            
            assertEquals(expected, e.getMessage());
            
            assertEquals(e.getCause().getClass(), WantedDiffersFromActual.class);
            
            String expectedCause = 
                "\n" +
                "Actual invocation:" +
                "\n" +
                "IMethods.simpleMethod(1)";
            
            assertEquals(expectedCause, e.getCause().getMessage());
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
    
    @Test
    public void shouldPrintWrongNumberOfInvocations() {
        strictly.verify(one).simpleMethod(1);
        strictly.verify(one).simpleMethod(11);
        try {
            strictly.verify(two, 1).simpleMethod(2);
            fail();
        } catch (TooManyActualInvocationsError e) {
            String actualMessage = e.getMessage();
            String expectedMessage = 
                    "\n" +
                    "IMethods.simpleMethod(2)" +
                    "\n" +
                    "Wanted 1 time but was 2"; 
            assertEquals(expectedMessage, actualMessage);         
        }
    }  
    
    @Test
    public void shouldPrintSequenceNumberWhenMocksAndMethodsAreTheSame() {
        StateResetter.reset();
        one = mock(IMethods.class);
        two = mock(IMethods.class);
        
        one.simpleMethod();
        two.simpleMethod();
        
        strictly = createStrictOrderVerifier(one, two);
        
        try {
            strictly.verify(two).simpleMethod();
            fail();
        } catch (VerificationError expected) {
            assertThat(expected, messageContains("IMethods#3.simpleMethod()"));
            assertThat(expected, causeMessageContains("IMethods#1.simpleMethod()"));
        }
    }
    
    @Ignore
    @Test
    public void shouldPrintSequenceNumberAndMatchersWhenMocksAndMethodsAreTheSame() {
        StateResetter.reset();
        one = mock(IMethods.class);
        two = mock(IMethods.class);
        
        one.simpleMethod(1);
        two.simpleMethod(1);
        
        strictly = createStrictOrderVerifier(one, two);
        
        try {
            strictly.verify(two).simpleMethod(Mockito.anyInt());
            fail();
        } catch (VerificationError expected) {
            assertThat(expected, messageContains("IMethods#3.simpleMethod(<any>)"));
            assertThat(expected, causeMessageContains("IMethods#1.simpleMethod(1)"));
        }
    }
}