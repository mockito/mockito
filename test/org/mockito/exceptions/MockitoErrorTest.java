package org.mockito.exceptions;

import org.junit.*;
import static org.mockito.util.ExtraMatchers.*;

import static org.junit.Assert.*;

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
