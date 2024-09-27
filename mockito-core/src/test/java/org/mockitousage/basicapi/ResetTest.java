/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ResetTest extends TestBase {

    @Mock private IMethods mock;

    @Mock private IMethods mockTwo;

    @Test
    public void shouldResetOngoingStubbingSoThatMoreMeaningfulExceptionsAreRaised() {
        mock.booleanReturningMethod();
        reset(mock);
        try {
            when(null).thenReturn("anything");
            fail();
        } catch (MissingMethodInvocationException e) {
        }
    }

    @Test
    public void resettingNonMockIsSafe() {
        assertThatThrownBy(
                        () -> {
                            reset("");
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessage("Argument should be a mock, but is: class java.lang.String");
    }

    @Test
    public void resettingNullIsSafe() {
        assertThatThrownBy(
                        () -> {
                            reset(new Object[] {null});
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessage("Argument should be a mock, but is null!");
    }

    @Test
    public void shouldRemoveAllStubbing() {
        when(mock.objectReturningMethod(isA(Integer.class))).thenReturn(100);
        when(mock.objectReturningMethod(200)).thenReturn(200);
        reset(mock);
        assertNull(mock.objectReturningMethod(200));
        assertEquals(
                "default behavior should return null", null, mock.objectReturningMethod("blah"));
    }

    @Test
    public void shouldRemoveAllInteractions() {
        mock.simpleMethod(1);
        reset(mock);
        verifyNoInteractions(mock);
    }

    @Test
    public void shouldRemoveAllInteractionsVerifyNoInteractions() {
        mock.simpleMethod(1);
        reset(mock);
        verifyNoInteractions(mock);
    }

    @Test
    public void shouldRemoveStubbingToString() {
        IMethods mockTwo = mock(IMethods.class);
        when(mockTwo.toString()).thenReturn("test");
        reset(mockTwo);
        assertThat(mockTwo.toString()).contains("Mock for IMethods");
    }

    @Test
    public void shouldStubbingNotBeTreatedAsInteractionVerifyNoInteractions() {
        when(mock.simpleMethod("one")).thenThrow(new RuntimeException());
        doThrow(new RuntimeException()).when(mock).simpleMethod("two");
        reset(mock);
        verifyNoInteractions(mock);
    }

    @Test
    public void shouldNotAffectMockName() {
        IMethods mock = mock(IMethods.class, "mockie");
        IMethods mockTwo = mock(IMethods.class);
        reset(mock);
        assertThat(mockTwo.toString()).contains("Mock for IMethods");
        assertEquals("mockie", "" + mock);
    }

    @Test
    public void shouldResetMultipleMocks() {
        mock.simpleMethod();
        mockTwo.simpleMethod();
        reset(mock, mockTwo);
        verifyNoMoreInteractions(mock, mockTwo);
    }

    @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
    @Test
    public void shouldValidateStateWhenResetting() {
        // invalid verify:
        verify(mock);

        try {
            reset(mockTwo);
            fail();
        } catch (UnfinishedVerificationException e) {
        }
    }

    @Test
    public void shouldMaintainPreviousDefaultAnswer() {
        // given
        mock = mock(IMethods.class, RETURNS_MOCKS);
        // when
        reset(mock);
        // then
        assertNotNull(mock.iMethodsReturningMethod());
    }
}
