/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockitoutil.TestBase;

public class WithinRangeTest extends TestBase {

    @Test
    public void shouldNotAllowNegativeMinNumberOfInvocations() throws Exception {
        try {
            VerificationModeFactory.withinRange(-50, 50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative values are not allowed here", e.getMessage());
        }
    }

    @Test
    public void shouldNotAllowNegativeMaxNumberOfInvocations() throws Exception {
        try {
            VerificationModeFactory.withinRange(50, -50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative values are not allowed here", e.getMessage());
        }
    }
    @Test
    public void shouldNotAllowMinNumberOfInvocationsToBeHigherThanMaxOne() throws Exception {
        try {
            VerificationModeFactory.withinRange(2, 1);
            fail();
        } catch (MockitoException e) {
            assertEquals("Minimum number of invocations cannot be higher than max number of invocations", e.getMessage());
        }
    }
}
