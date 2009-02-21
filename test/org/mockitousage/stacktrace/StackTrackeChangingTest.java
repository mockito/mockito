/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stacktrace;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class StackTrackeChangingTest extends TestBase {
    
    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }
    
    private void simpleMethodOnAMock() {
        mock.simpleMethod("blah");
    }
    
    private void verifySimpleMethodOnAMock() {
        verify(mock).simpleMethod();        
    }
    
    @Test
    public void shouldShowActualInvocationAsExceptionCause() {
        simpleMethodOnAMock();
        try {
            verifySimpleMethodOnAMock();
            fail();
        } catch (AssertionError e) {
            String expected = 
                "\nArgument(s) are different! Wanted:"+
                "\niMethods.simpleMethod();"+
                "\n-> at org.mockitousage.stacktrace.StackTrackeChangingTest.verifySimpleMethodOnAMock(StackTrackeChangingTest.java:29)"+
                "\nActual invocation has different arguments:"+
                "\niMethods.simpleMethod(\"blah\");"+
                "\n-> at org.mockitousage.stacktrace.StackTrackeChangingTest.simpleMethodOnAMock(StackTrackeChangingTest.java:25)" +
                "\n";
             
            assertEquals(expected, e.getMessage());
        }
    }
}