/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.parents;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.util.RequiresValidState;

public class MockitoExceptionTest extends RequiresValidState {

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
