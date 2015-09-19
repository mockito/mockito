/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockitoutil.TestBase;

public class TimesTest extends TestBase {

    @Test
    public void shouldNotAllowNegativeNumberOfInvocations() throws Exception {
        try {
            VerificationModeFactory.times(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }
}
