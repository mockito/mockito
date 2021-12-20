/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ResetInvocationsTest extends TestBase {

    @Mock IMethods methods;

    @Mock IMethods moarMethods;

    @Test
    public void reset_invocations_should_reset_only_invocations() {
        when(methods.simpleMethod()).thenReturn("return");

        methods.simpleMethod();
        verify(methods).simpleMethod();

        clearInvocations(methods);

        verifyNoMoreInteractions(methods);
        assertEquals("return", methods.simpleMethod());
    }

    @Test
    public void should_reset_invocations_on_multiple_mocks() {
        methods.simpleMethod();
        moarMethods.simpleMethod();

        clearInvocations(methods, moarMethods);

        verifyNoMoreInteractions(methods, moarMethods);
    }

    @Test
    public void resettingNonMockIsSafe() {
        assertThatThrownBy(
                        () -> {
                            clearInvocations("");
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessage("Argument should be a mock, but is: class java.lang.String");
    }

    @Test
    public void resettingNullIsSafe() {
        assertThatThrownBy(
                        () -> {
                            clearInvocations(new Object[] {null});
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessage("Argument should be a mock, but is null!");
    }
}
