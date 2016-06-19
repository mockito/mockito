/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class AtLeastTest extends TestBase {

    @Test
    public void shouldNotAllowNegativeNumberOfMinimumInvocations() throws Exception {
        try {
            VerificationModeFactory.atLeast(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }

    @Test
    public void shouldAllowZeroInvocations() throws Exception {
        VerificationModeFactory.atLeast(0);
    }
}
