package org.easymock.tests2;

import org.easymock.IMocksControl;
import org.easymock.tests.IMethods;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class NameTest {
    @Test
    public void nameForMock() {
        IMethods mock = createMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        try {
            verify(mock);
        } catch (AssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = "\n  Expectation failure on verify:\n    mock.simpleMethod(): expected: 1, actual: 0";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    @Test
    public void nameForStrictMock() {
        IMethods mock = createStrictMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        try {
            verify(mock);
        } catch (AssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = "\n  Expectation failure on verify:\n    mock.simpleMethod(): expected: 1, actual: 0";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    @Test
    public void nameForNiceMock() {
        IMethods mock = createNiceMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        try {
            verify(mock);
        } catch (AssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = "\n  Expectation failure on verify:\n    mock.simpleMethod(): expected: 1, actual: 0";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    @Test
    public void nameForMocksControl() {
        IMocksControl control = createControl();
        IMethods mock = control.createMock("mock", IMethods.class);
        mock.simpleMethod();
        replay(mock);
        try {
            verify(mock);
        } catch (AssertionError expected) {
            String actualMessage = expected.getMessage();
            String expectedMessage = "\n  Expectation failure on verify:\n    mock.simpleMethod(): expected: 1, actual: 0";
            assertEquals(expectedMessage, actualMessage);         
        }
    }
    
    @Test
    public void shouldThrowIllegalArgumentExceptionIfNameIsNoValidJavaIdentifier() {
        try {
            createMock("no-valid-java-identifier", IMethods.class);
            throw new AssertionError();
        } catch (IllegalArgumentException expected) {
            assertEquals("'no-valid-java-identifier' is not a valid Java identifier.", expected.getMessage());
        }
    }

}
