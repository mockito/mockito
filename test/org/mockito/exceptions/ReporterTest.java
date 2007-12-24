/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.verification.TooLittleActualInvocations;

public class ReporterTest extends RequiresValidState {

    @Test(expected=TooLittleActualInvocations.class)
    public void shouldLetPassingNullLastActualStackTrace() throws Exception {
        new Reporter().tooLittleActualInvocations(1, 2, "wanted", null);
    }
}