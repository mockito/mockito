/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockitoutil.TestBase;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;

public class DetectingFinalMethodsTest extends TestBase {

    class WithFinal {
        final int foo() {
            return 0;
        }
    }

    @Mock private WithFinal withFinal;

    @Test
    public void shouldFailWithUnfinishedVerification() {
        assumeTrue("Does not apply for inline mocks", withFinal.getClass() != WithFinal.class);
        verify(withFinal).foo();
        try {
            verify(withFinal).foo();
            fail();
        } catch (UnfinishedVerificationException e) {}
    }

    @Test
    public void shouldFailWithUnfinishedStubbing() {
        assumeTrue("Does not apply for inline mocks", withFinal.getClass() != WithFinal.class);
        withFinal = mock(WithFinal.class);
        try {
            when(withFinal.foo()).thenReturn(null);
            fail();
        } catch (MissingMethodInvocationException e) {}
    }
}
