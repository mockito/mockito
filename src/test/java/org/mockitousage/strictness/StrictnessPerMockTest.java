/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.strictness;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.withSettings;

//TODO 792 also move other Strictness tests to this package (unless they already have good package)
public class StrictnessPerMockTest {

    MockitoSession mockito;
    @Mock IMethods strictStubsMock;
    IMethods lenientMock;

    @Before
    public void before() {
        mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_STUBS).startMocking();
        assertNull(lenientMock);
        lenientMock = mock(IMethods.class, withSettings().lenient());
    }

    @Test
    public void knows_if_mock_is_lenient() {
        assertTrue(mockingDetails(lenientMock).getMockCreationSettings().isLenient());
        assertFalse(mockingDetails(strictStubsMock).getMockCreationSettings().isLenient());
    }

    @Test
    public void potential_stubbing_problem() {
        //when
        given(lenientMock.simpleMethod(100)).willReturn("100");
        given(strictStubsMock.simpleMethod(100)).willReturn("100");

        //then on lenient mock (created by hand), we can call the stubbed method with different arg:
        lenientMock.simpleMethod(200);

        //and on strict stub mock (created by session), we cannot call stubbed method with different arg:
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() throws Throwable {
                ProductionCode.simpleMethod(strictStubsMock, 200);
            }
        }).isInstanceOf(PotentialStubbingProblem.class);
    }

    @Test
    public void unnecessary_stubbing() {
        //when
        given(lenientMock.simpleMethod(100)).willReturn("100");
        given(strictStubsMock.simpleMethod(100)).willReturn("100");

        //then unnecessary stubbing flags method only on the strict stub mock:
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                mockito.finishMocking();
            }
        }).isInstanceOf(UnnecessaryStubbingException.class)
            .hasMessageContaining("1. -> ")
            //good enough to prove that we're flagging just one unnecessary stubbing:
            //TODO 792: let's make UnnecessaryStubbingException exception contain the Invocation instance
            //so that we can write clean assertion rather than depending on string
            .isNot(TestBase.hasMessageContaining("2. ->"));
    }

    @Test
    public void verify_no_more_invocations() {
        //when
        given(lenientMock.simpleMethod(100)).willReturn("100");
        given(strictStubsMock.simpleMethod(100)).willReturn("100");

        //and:
        strictStubsMock.simpleMethod(100);
        lenientMock.simpleMethod(100);

        //then 'verifyNoMoreInteractions' ignores strict stub (implicitly verified) but flags the lenient mock
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                verifyNoMoreInteractions(strictStubsMock, lenientMock);
            }
        }).isInstanceOf(NoInteractionsWanted.class)
            .hasMessageContaining("But found this interaction on mock 'iMethods'")
            //TODO 792: let's make NoInteractionsWanted exception contain the Invocation instances
            //so that we can write clean assertion rather than depending on string
            .hasMessageContaining("Actually, above is the only interaction with this mock");
    }

    @After
    public void after() {
        mockito.finishMocking();
    }
}
