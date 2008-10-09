/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static org.mockito.internal.verification.VerificationModeImpl.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

public class VerificationModeImplTest extends TestBase {

    @Test
    public void shouldNotAllowNegativeNumberOfInvocations() throws Exception {
        try {
            times(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }

    @Test
    public void shouldNotAllowNegativeNumberOfMinimumInvocations() throws Exception {
        try {
            atLeast(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value or zero are not allowed here", e.getMessage());
        }
    }
}