/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.mock;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class NoInteractionsTest extends TestBase {

    @Test
    public void noInteractionsExceptionMessageShouldDescribeMock() {
        // given
        NoInteractions n = new NoInteractions();
        IMethods mock = mock(IMethods.class, "a mock");
        InvocationMatcher i = new InvocationBuilder().mock(mock).toInvocationMatcher();

        InvocationContainerImpl invocations = new InvocationContainerImpl(new MockSettingsImpl());
        invocations.setInvocationForPotentialStubbing(i);

        try {
            // when
            n.verify(new VerificationDataImpl(invocations, null));
            // then
            fail();
        } catch (NoInteractionsWanted e) {
            Assertions.assertThat(e.toString()).contains(mock.toString());
        }
    }
}
