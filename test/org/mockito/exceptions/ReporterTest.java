/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

import org.junit.Test;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockitoutil.TestBase;

public class ReporterTest extends TestBase {

    @Test(expected=TooLittleActualInvocations.class)
    public void shouldLetPassingNullLastActualStackTrace() throws Exception {
        new Reporter().tooLittleActualInvocations(new Discrepancy(1, 2), new InvocationBuilder().toInvocation(), null);
    }
}