/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class VerificationExcludingStubsTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void shouldAllowToExcludeStubsForVerification() {
        // given
        when(mock.simpleMethod()).thenReturn("foo");

        // when
        String stubbed = mock.simpleMethod(); // irrelevant call because it is stubbing
        mock.objectArgMethod(stubbed);

        // then
        verify(mock).objectArgMethod("foo");

        // verifyNoMoreInteractions fails:
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
        }

        // but it works when stubs are ignored:
        Object[] ignored = ignoreStubs(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldExcludeFromVerificationInOrder() {
        // given
        when(mock.simpleMethod()).thenReturn("foo");

        // when
        mock.objectArgMethod("1");
        mock.objectArgMethod("2");
        mock.simpleMethod(); // calling the stub

        // then
        InOrder inOrder = inOrder(ignoreStubs(mock));
        inOrder.verify(mock).objectArgMethod("1");
        inOrder.verify(mock).objectArgMethod("2");
        inOrder.verifyNoMoreInteractions();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldIgnoringStubsDetectNulls() {
        assertThatThrownBy(
                        () -> {
                            Object ignored = ignoreStubs(mock, null);
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessage("Argument should be a mock, but is null!");
    }

    @Test
    public void shouldIgnoringStubsDetectNonMocks() {
        assertThatThrownBy(
                        () -> {
                            Object ignored = ignoreStubs(mock, new Object());
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessage("Argument should be a mock, but is: class java.lang.Object");
    }
}
