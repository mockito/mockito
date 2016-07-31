/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ResetTest extends TestBase {

    @Mock
    private IMethods mock;

    @Mock
    private IMethods mockTwo;

    @Test
    public void shouldResetOngoingStubbingSoThatMoreMeaningfulExceptionsAreRaised() {
        mock(IMethods.class);
        mock.booleanReturningMethod();
        reset(mock);
        try {
            when(null).thenReturn("anything");
            fail();
        } catch (MissingMethodInvocationException e) {
        }
    }

    @Test(expected = NotAMockException.class)
    public void resettingNonMockIsSafe() {
        reset("");
    }

    @Test(expected = NotAMockException.class)
    public void resettingNullIsSafe() {
        reset(new Object[]{null});
    }

    @Test
    public void shouldRemoveAllStubbing() throws Exception {
        when(mock.objectReturningMethod(isA(Integer.class))).thenReturn(100);
        when(mock.objectReturningMethod(200)).thenReturn(200);
        reset(mock);
        assertNull(mock.objectReturningMethod(200));
        assertEquals("default behavior should return null", null, mock.objectReturningMethod("blah"));
    }

    @Test
    public void shouldRemoveAllInteractions() throws Exception {
        mock.simpleMethod(1);
        reset(mock);
        verifyZeroInteractions(mock);
    }

    @Test
    public void shouldRemoveStubbingToString() throws Exception {
        IMethods mockTwo = mock(IMethods.class);
        when(mockTwo.toString()).thenReturn("test");
        reset(mockTwo);
        assertThat(mockTwo.toString()).contains("Mock for IMethods");
    }

    @Test
    public void shouldStubbingNotBeTreatedAsInteraction() {
        when(mock.simpleMethod("one")).thenThrow(new RuntimeException());
        doThrow(new RuntimeException()).when(mock).simpleMethod("two");
        reset(mock);
        verifyZeroInteractions(mock);
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

    @Test
    public void shouldValidateStateWhenResetting() {
        //invalid verify:
        verify(mock);

        try {
            reset(mockTwo);
            fail();
        } catch (UnfinishedVerificationException e) {
        }
    }

    @Test
    public void shouldMaintainPreviousDefaultAnswer() {
        //given
        mock = mock(IMethods.class, RETURNS_MOCKS);
        //when
        reset(mock);
        //then
        assertNotNull(mock.iMethodsReturningMethod());
    }
}