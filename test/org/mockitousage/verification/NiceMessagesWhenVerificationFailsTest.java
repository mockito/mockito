/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;
import static org.mockito.util.ExtraMatchers.contains;
import static org.mockito.CrazyMatchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.VerificationError;
import org.mockito.util.RequiresValidState;
import org.mockitousage.IMethods;

public class NiceMessagesWhenVerificationFailsTest extends RequiresValidState {
    
    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }
    
    @Test
    public void shouldPrintMethodName() {
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (VerificationError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
            		"Wanted but not invoked:" +
            		"\n" +
            		"IMethods.simpleMethod()";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    private class SomeClass {
        public String toString() {
            return "SomeClass instance";
        }
    }
    
    @Test
    public void shouldPrintMethodNameAndArguments() {
        try {
            verify(mock).threeArgumentMethod(12, new SomeClass(), "some string");
            fail();
        } catch (VerificationError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
            		"IMethods.threeArgumentMethod(12, SomeClass instance, \"some string\")";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Test
    public void shouldPrintLastUnverifiedAsActualInvocation() {
        mock.oneArg(true);
        mock.simpleMethod();
        
        verify(mock).oneArg(true);
        try {
            verify(mock).twoArgumentMethod(1,2);
            fail();
        } catch (VerificationError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
                    "IMethods.twoArgumentMethod(1, 2)";
            
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Test
    public void shouldPrintActualAndWantedWhenTheDifferenceIsAboutArguments() {
        mock.oneArg(true);
        mock.twoArgumentMethod(1, 2);
        
        verify(mock).oneArg(true);
        try {
            verify(mock).twoArgumentMethod(1, 1000);
            fail();
        } catch (VerificationError e) {
            String expected = 
                    "\n" +
                    "Invocation differs from actual" +
                    "\n" +
                    "Wanted invocation:" +
                    "\n" +
                    "IMethods.twoArgumentMethod(1, 1000)";
            
            assertEquals(expected, e.getMessage());
            
            String expectedCause =
                    "\n" +
                    "Actual invocation:" +
                    "\n" +
                    "IMethods.twoArgumentMethod(1, 2)";
            
            assertEquals(expectedCause, e.getCause().getMessage());      
        }
    }
    
    @Test
    public void shouldPrintActualAndWantedWhenActualMethodNameAndWantedMethodNameAreTheSame() {
        mock.simpleMethod();
        
        try {
            verify(mock).simpleMethod("test");
            fail();
        } catch (VerificationError e) {
            assertThat(e.getMessage(), contains("IMethods.simpleMethod(\"test\")"));
            assertThat(e.getCause().getMessage(), contains("IMethods.simpleMethod()"));
        }
    }    
    
    @Test
    public void shouldPrintActualAndUnverifiedWantedWhenTheDifferenceIsAboutArguments() {
        mock.twoArgumentMethod(1, 1);
        mock.twoArgumentMethod(2, 2);
        mock.twoArgumentMethod(3, 3);
        
        verify(mock).twoArgumentMethod(1, 1);
        verify(mock).twoArgumentMethod(2, 2);
        try {
            verify(mock).twoArgumentMethod(3, 1000);
            fail();
        } catch (VerificationError e) {
            assertThat(e.getMessage(), contains("IMethods.twoArgumentMethod(3, 1000)"));
            assertThat(e.getCause().getMessage(), contains("IMethods.twoArgumentMethod(3, 3)"));
        }
    }  
    
    @Test
    public void shouldPrintFirstUnexpectedInvocation() {
        mock.oneArg(true);
        mock.oneArg(false);
        mock.threeArgumentMethod(1, "2", "3");
        
        verify(mock).oneArg(true);
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (VerificationError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
            		"No more interactions wanted" +
            		"\n" +
            		"Unexpected: IMethods.oneArg(false)";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Test
    public void shouldPrintFirstUnexpectedInvocationWhenVerifyingZeroInteractions() {
        mock.twoArgumentMethod(1, 2);
        mock.threeArgumentMethod(1, "2", "3");
        
        try {
            verifyZeroInteractions(mock);
            fail();
        } catch (VerificationError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Zero interactions wanted" +
                    "\n" +
                    "Unexpected: IMethods.twoArgumentMethod(1, 2)";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Test
    public void shouldPrintMethodNameWhenVerifyingAtLeastOnce() throws Exception {
        try {
            verify(mock, atLeastOnce()).twoArgumentMethod(1, 2);
            fail();
        } catch (VerificationError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                "\n" +
                "Wanted but not invoked:" +
                "\n" +
                "IMethods.twoArgumentMethod(1, 2)";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Test
    public void shouldPrintMethodNicelyWhenMatcherUsed() throws Exception {
        try {
            verify(mock, atLeastOnce()).twoArgumentMethod(anyInt(), eq(100));
            fail();
        } catch (VerificationError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                "\n" +
                "Wanted but not invoked:" +
                "\n" +
                "IMethods.twoArgumentMethod(<any>, 100)";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Test
    public void shouldPrintMethodNicelyWhenMissingInvocationWithArrayMatcher() {
        mock.oneArray(new boolean[] { true, false, false });
        
        try {
            verify(mock).oneArray(aryEq(new boolean[] { false, false, false }));
            fail();
        } catch (VerificationError e) {
            assertThat(e.getMessage(), contains("IMethods.oneArray([false, false, false])"));
            assertThat(e.getCause().getMessage(), contains("IMethods.oneArray([true, false, false])"));
        }
    }
    
    @Test
    public void shouldPrintMethodNicelyWhenMissingInvocationWithVarargMatcher() {
        mock.varargsString(10, "one", "two");
        
        try {
            verify(mock).varargsString(10, "two", "one");
            fail();
        } catch (VerificationError e) {
            assertThat(e.getMessage(), contains("IMethods.varargsString(10, \"two\", \"one\")"));
            assertThat(e.getCause().getMessage(), contains("IMethods.varargsString(10, \"one\", \"two\")"));
        }
    }
    
    @Test
    public void shouldPrintMethodNicelyWhenMissingInvocationWithMatcher() {
        mock.simpleMethod("foo");
        
        try {
            verify(mock).simpleMethod(matches("burrito"));
            fail();
        } catch (VerificationError e) {
            assertThat(e.getMessage(), contains("IMethods.simpleMethod(matches(\"burrito\"))"));
            assertThat(e.getCause().getMessage(), contains("IMethods.simpleMethod(\"foo\")"));
        }
    }
    
    @Test
    public void shouldPrintNullArguments() throws Exception {
        mock.simpleMethod(null, null);
        try {
            verify(mock).simpleMethod("test");
            fail();
        } catch (VerificationError e) {
            assertThat(e.getCause().getMessage(), contains("simpleMethod(null, null)"));
        }
    }
}