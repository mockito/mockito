/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitoutil.TestBase;

// see issue 112
public class AtLeastMarksAllInvocationsVerified extends TestBase {

    public static class SomeMethods {
        public void allowedMethod() {}

        public void disallowedMethod() {}
    }

    @Test
    public void shouldFailBecauseDisallowedMethodWasCalled() {
        SomeMethods someMethods = mock(SomeMethods.class);

        someMethods.allowedMethod();
        someMethods.disallowedMethod();

        verify(someMethods, atLeast(1)).allowedMethod();
        assertThatThrownBy(
                        () -> {
                            verifyNoMoreInteractions(someMethods);
                        })
                .isInstanceOf(NoInteractionsWanted.class)
                .hasMessageContainingAll(
                        "No interactions wanted here:",
                        "-> at ",
                        "But found this interaction on mock 'someMethods':",
                        "-> at ",
                        "For your reference, here is the list of all invocations ([?] - means unverified).",
                        "1. -> at ",
                        "2. [?]-> at ");
    }
}
