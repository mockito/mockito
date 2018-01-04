/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.withSettings;

public class StrictnessPerMockTest {

    MockitoSession mockito;
    @Mock IMethods mock;
    IMethods lenientMock = mock(IMethods.class, withSettings().strictness(Strictness.LENIENT));

    @Before
    public void setup() {
         mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();
    }

    @After
    public void after() {
        mockito.finishMocking();
    }

    @Test public void strictness_per_mock() throws Throwable {
        //when
        given(lenientMock.simpleMethod(100)).willReturn("100");
        given(mock.simpleMethod(100)).willReturn("100");

        //on lenient mock, we can call the stubbed method with different argument:
        lenientMock.simpleMethod(200);
        //and stubbing will be reported in 'verifyNoMoreInteractions'
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                verifyNoMoreInteractions(lenientMock);
            }
        }).isInstanceOf(NoInteractionsWanted.class);

        //on the mock with strict stubs, the stubbing is implicitly verified so 'verifyNoMoreInteractions' succeeds:
        verifyNoMoreInteractions(mock);
        //and we will get strict stubbing exception if there is declared stubbing with different argument:
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                mock.simpleMethod(200);
            }
        }).isInstanceOf(PotentialStubbingProblem.class);
    }

    @Test public void strictness_per_stubbing() throws Throwable {
        //when
        given(mock.simpleMethod("1")).willReturn("1");

        lenient().when(mock.differentMethod("1")).thenReturn("1");

        //on lenient mock, we can call the stubbed method with different argument:
        mock.differentMethod("200");

        //on other mock, we will get strict stubbing exception
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                mock.simpleMethod(200);
            }
        }).isInstanceOf(PotentialStubbingProblem.class);
    }
}
