package org.mockito.usage.verification;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.MockVerificationAssertionError;
import org.mockito.usage.IMethods;

public class NiceMessagesWhenVerificationFailsTest {
    
    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }
    
    @Test
    public void shouldPrintMethodName() {
        try {
            verify(mock).simpleMethod();
        } catch (MockVerificationAssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
            		"Not invoked: IMethods.simpleMethod()";
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
        } catch (MockVerificationAssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
            		"Not invoked: IMethods.threeArgumentMethod(12, SomeClass instance, \"some string\")";
            assertEquals(expectedMessage, actualMessage);         
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
        } catch (MockVerificationAssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
            		"No more interactions expected on IMethods but found: IMethods.oneArg(false)";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Test
    public void shouldPrintFirstUnexpectedInvocationWhenVerifyingZeroInteractions() {
        mock.twoArgumentMethod(1, 2);
        mock.threeArgumentMethod(1, "2", "3");
        
        try {
            verifyZeroInteractions(mock);
        } catch (MockVerificationAssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = 
                    "\n" +
                    "Zero interactions expected on IMethods but found: IMethods.twoArgumentMethod(1, 2)";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
}
