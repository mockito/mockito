package org.easymock;

import org.easymock.IMocksControl;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

//TODO change this test so it asserts nice exception messages and method name is displayed 
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
}
