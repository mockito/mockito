package org.mockito.usage.verification;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.MockVerificationAssertionError;
import org.mockito.usage.IMethods;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NiceMessagesWhenVerificationFailsTest {
    
    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }
    
    @Ignore
    @Test
    public void shouldPrintMethodName() {
        try {
            verify(mock).simpleMethod();
        } catch (MockVerificationAssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = "\n  Method not invoked: IMethods.simpleMethod()";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Ignore
    @Test
    public void shouldPrintMethodNameAndArguments() {
        try {
            verify(mock).threeArgumentMethod(12, new Object(), "some string");
        } catch (MockVerificationAssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = "\n  Method not invoked: IMethods.threeArgumentMethod(12, id1234123, \"some string\")";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Ignore
    @Test
    public void shouldPrintFirstUnexpectedInvocation() {
        mock.oneArg(true);
        mock.threeArgumentMethod(1, "2", "3");
        try {
            verifyNoMoreInteractions(mock);
        } catch (MockVerificationAssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = "\n  No interactions expected but found: IMethods.oneArg(true)";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
}
