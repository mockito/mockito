/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.internal.invocation.InvocationBuilder;

public class ReporterTest extends TestBase {

    @Test(expected=TooLittleActualInvocations.class)
    public void shouldLetPassingNullLastActualStackTrace() throws Exception {
        new Reporter().tooLittleActualInvocations(1, 2, new InvocationBuilder().toInvocation(), null);
    }
}