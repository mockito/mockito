package org.mockito.exceptions;

import static org.junit.Assert.*;

import org.junit.Test;

public class MockitoErrorTest {

    private void throwIt() {
        throw new MockitoException("boom");
    }
    
    @Test
    public void shouldKeepUnfilteredStackTrace() {
        try {
            throwIt();
            fail();
        } catch (MockitoException e) {
            assertEquals("throwIt", e.getUnfilteredStackTrace()[0].getMethodName());
        }
    }
}
