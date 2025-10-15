/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class NoMoreInteractionsVerificationTest extends TestBase {

    private LinkedList mock;

    @Before
    public void setup() {
        mock = mock(LinkedList.class);
    }

    @Test
    public void shouldStubbingNotRegisterRedundantInteractions() {
        when(mock.add("one")).thenReturn(true);
        when(mock.add("two")).thenReturn(true);

        mock.add("one");

        verify(mock).add("one");
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldVerifyWhenWantedNumberOfInvocationsUsed() {
        mock.add("one");
        mock.add("one");
        mock.add("one");

        verify(mock, times(3)).add("one");

        verifyNoMoreInteractions(mock);
    }

    @Test
    public void shouldFailNoMoreInteractionsVerification() {
        mock.clear();

        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
        }
    }

    @Test
    public void shouldFailNoInteractionsVerification() {
        mock.clear();

        try {
            verifyNoInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
        }
    }

    @Test
    public void shouldPrintAllInvocationsWhenVerifyingNoMoreInvocations() {
        mock.add(1);
        mock.add(2);
        mock.clear();

        verify(mock).add(2);
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
            assertThat(e).hasMessageContaining("list of all invocations");
        }
    }

    @Test
    public void shouldNotContainAllInvocationsWhenSingleUnwantedFound() {
        mock.add(1);

        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
            assertThat(e.getMessage()).doesNotContain("list of all invocations");
        }
    }

    @Test
    public void shouldVerifyOneMockButFailOnOther() {
        List<String> list = mock(List.class);
        Map<String, Integer> map = mock(Map.class);

        list.add("one");
        list.add("one");

        map.put("one", 1);

        verify(list, times(2)).add("one");

        verifyNoMoreInteractions(list);
        try {
            verifyNoInteractions(map);
            fail();
        } catch (NoInteractionsWanted e) {
        }
    }

    @Test
    public void shouldVerifyOneMockButFailOnOtherVerifyNoInteractions() {
        List<String> list = mock(List.class);
        Map<String, Integer> map = mock(Map.class);

        list.add("one");
        list.add("one");

        map.put("one", 1);

        verify(list, times(2)).add("one");

        verifyNoMoreInteractions(list);
        try {
            verifyNoInteractions(map);
            fail();
        } catch (NoInteractionsWanted e) {
        }
    }

    // @SuppressWarnings("all")
    @Test
    public void verifyNoMoreInteractionsShouldScreamWhenNullPassed() {
        assertThatThrownBy(
                        () -> {
                            verifyNoMoreInteractions((Object[]) null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Method requires argument(s)!",
                        "Pass mocks that should be verified, e.g:",
                        "    verifyNoMoreInteractions(mockOne, mockTwo);",
                        "    verifyNoInteractions(mockOne, mockTwo);");
    }
}
