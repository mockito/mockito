/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.base.MockitoException;

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
