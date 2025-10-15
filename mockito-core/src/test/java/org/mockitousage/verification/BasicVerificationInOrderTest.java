/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class BasicVerificationInOrderTest extends TestBase {

    private IMethods mockOne;
    private IMethods mockTwo;
    private IMethods mockThree;
    private InOrder inOrder;

    @Before
    public void setUp() {
        mockOne = mock(IMethods.class);
        mockTwo = mock(IMethods.class);
        mockThree = mock(IMethods.class);

        inOrder = inOrder(mockOne, mockTwo, mockThree);

        mockOne.simpleMethod(1);
        mockTwo.simpleMethod(2);
        mockTwo.simpleMethod(2);
        mockThree.simpleMethod(3);
        mockTwo.simpleMethod(2);
        mockOne.simpleMethod(4);
    }

    @Test
    public void shouldVerifyInOrder() {
        inOrder.verify(mockOne).simpleMethod(1);
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        inOrder.verify(mockThree).simpleMethod(3);
        inOrder.verify(mockTwo).simpleMethod(2);
        inOrder.verify(mockOne).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    }

    @Test
    public void shouldVerifyInOrderUsingAtLeastOnce() {
        inOrder.verify(mockOne, atLeastOnce()).simpleMethod(1);
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        inOrder.verify(mockThree).simpleMethod(3);
        inOrder.verify(mockTwo).simpleMethod(2);
        inOrder.verify(mockOne, atLeastOnce()).simpleMethod(4);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    }

    @Test
    public void shouldVerifyInOrderWhenExpectingSomeInvocationsToBeCalledZeroTimes() {
        inOrder.verify(mockOne, times(0)).oneArg(false);
        inOrder.verify(mockOne).simpleMethod(1);
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        inOrder.verify(mockTwo, times(0)).simpleMethod(22);
        inOrder.verify(mockThree).simpleMethod(3);
        inOrder.verify(mockTwo).simpleMethod(2);
        inOrder.verify(mockOne).simpleMethod(4);
        inOrder.verify(mockThree, times(0)).oneArg(false);
        verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
    }

    @Test
    public void shouldFailWhenFirstMockCalledTwice() {
        inOrder.verify(mockOne).simpleMethod(1);
        try {
            inOrder.verify(mockOne).simpleMethod(1);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailWhenLastMockCalledTwice() {
        inOrder.verify(mockOne).simpleMethod(1);
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        inOrder.verify(mockThree).simpleMethod(3);
        inOrder.verify(mockTwo).simpleMethod(2);
        inOrder.verify(mockOne).simpleMethod(4);
        try {
            inOrder.verify(mockOne).simpleMethod(4);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailOnFirstMethodBecauseOneInvocationWanted() {
        assertThatThrownBy(
                        () -> {
                            inOrder.verify(mockOne, times(0)).simpleMethod(1);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "Verification in order failure:",
                        "iMethods.simpleMethod(1);",
                        "Wanted 0 times:",
                        "-> at ",
                        "But was 1 time:",
                        "-> at ");
    }

    @Test
    public void shouldFailOnFirstMethodBecauseOneInvocationWantedAgain() {
        assertThatThrownBy(
                        () -> {
                            inOrder.verify(mockOne, times(2)).simpleMethod(1);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "Verification in order failure:",
                        "iMethods.simpleMethod(1);",
                        "Wanted 2 times:",
                        "-> at ",
                        "But was 1 time:",
                        "-> at ");
    }

    @Test
    public void shouldFailOnSecondMethodBecauseFourInvocationsWanted() {
        inOrder.verify(mockOne, times(1)).simpleMethod(1);
        try {
            inOrder.verify(mockTwo, times(4)).simpleMethod(2);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailOnSecondMethodBecauseTwoInvocationsWantedAgain() {
        inOrder.verify(mockOne, times(1)).simpleMethod(1);
        try {
            inOrder.verify(mockTwo, times(0)).simpleMethod(2);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailOnLastMethodBecauseOneInvocationWanted() {
        inOrder.verify(mockOne, atLeastOnce()).simpleMethod(1);
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        inOrder.verify(mockThree, atLeastOnce()).simpleMethod(3);
        inOrder.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        try {
            inOrder.verify(mockOne, times(0)).simpleMethod(4);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailOnLastMethodBecauseOneInvocationWantedAgain() {
        inOrder.verify(mockOne, atLeastOnce()).simpleMethod(1);
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        inOrder.verify(mockThree, atLeastOnce()).simpleMethod(3);
        inOrder.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        try {
            inOrder.verify(mockOne, times(2)).simpleMethod(4);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    /* ------------- */

    @Test
    public void shouldFailOnFirstMethodBecauseDifferentArgsWanted() {
        assertThatThrownBy(
                        () -> {
                            inOrder.verify(mockOne).simpleMethod(100);
                        })
                .isInstanceOf(ArgumentsAreDifferent.class)
                .hasMessageContainingAll(
                        "Argument(s) are different! Wanted:",
                        "iMethods.simpleMethod(100);",
                        "-> at ",
                        "Actual invocations have different arguments:",
                        "iMethods.simpleMethod(1);",
                        "-> at ",
                        "iMethods.simpleMethod(2);",
                        "-> at ",
                        "iMethods.simpleMethod(2);",
                        "-> at ",
                        "iMethods.simpleMethod(3);",
                        "-> at ",
                        "iMethods.simpleMethod(2);",
                        "-> at ",
                        "iMethods.simpleMethod(4);",
                        "-> at ");
    }

    @Test
    public void shouldFailOnFirstMethodBecauseDifferentMethodWanted() {
        assertThatThrownBy(
                        () -> {
                            inOrder.verify(mockOne).oneArg(true);
                        })
                .isInstanceOf(WantedButNotInvoked.class)
                .hasMessageContainingAll(
                        "Wanted but not invoked:",
                        "iMethods.oneArg(true);",
                        "-> at ",
                        "However, there were exactly 6 interactions with this mock:",
                        "iMethods.simpleMethod(1);",
                        "-> at ",
                        "iMethods.simpleMethod(2);",
                        "-> at ",
                        "iMethods.simpleMethod(2);",
                        "-> at ",
                        "iMethods.simpleMethod(3);",
                        "-> at ",
                        "iMethods.simpleMethod(2);",
                        "-> at ",
                        "iMethods.simpleMethod(4);",
                        "-> at ");
    }

    @Test
    public void shouldFailOnSecondMethodBecauseDifferentArgsWanted() {
        inOrder.verify(mockOne).simpleMethod(1);
        try {
            inOrder.verify(mockTwo, times(2)).simpleMethod(-999);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailOnSecondMethodBecauseDifferentMethodWanted() {
        inOrder.verify(mockOne, times(1)).simpleMethod(1);
        try {
            inOrder.verify(mockTwo, times(2)).oneArg(true);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailOnLastMethodBecauseDifferentArgsWanted() {
        inOrder.verify(mockOne).simpleMethod(1);
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        inOrder.verify(mockThree).simpleMethod(3);
        inOrder.verify(mockTwo).simpleMethod(2);
        try {
            inOrder.verify(mockOne).simpleMethod(-666);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailOnLastMethodBecauseDifferentMethodWanted() {
        inOrder.verify(mockOne).simpleMethod(1);
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        inOrder.verify(mockThree).simpleMethod(3);
        inOrder.verify(mockTwo).simpleMethod(2);
        try {
            inOrder.verify(mockOne).oneArg(false);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    /* -------------- */

    @Test
    public void shouldFailWhenLastMethodVerifiedFirst() {
        inOrder.verify(mockOne).simpleMethod(4);
        try {
            inOrder.verify(mockOne).simpleMethod(1);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailWhenMiddleMethodVerifiedFirst() {
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        try {
            inOrder.verify(mockOne).simpleMethod(1);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailWhenMiddleMethodVerifiedFirstInAtLeastOnceMode() {
        inOrder.verify(mockTwo, atLeastOnce()).simpleMethod(2);
        try {
            inOrder.verify(mockOne).simpleMethod(1);
            fail();
        } catch (VerificationInOrderFailure e) {
        }
    }

    @Test
    public void shouldFailOnVerifyNoMoreInteractions() {
        inOrder.verify(mockOne).simpleMethod(1);
        inOrder.verify(mockTwo, times(2)).simpleMethod(2);
        inOrder.verify(mockThree).simpleMethod(3);
        inOrder.verify(mockTwo).simpleMethod(2);

        try {
            verifyNoMoreInteractions(mockOne, mockTwo, mockThree);
            fail();
        } catch (NoInteractionsWanted e) {
        }
    }

    @Test
    public void shouldFailOnVerifyNoInteractions() {
        assertThatThrownBy(
                        () -> {
                            verifyNoInteractions(mockOne);
                        })
                .isInstanceOf(NoInteractionsWanted.class)
                .hasMessageContainingAll(
                        "No interactions wanted here:",
                        "-> at ",
                        "But found these interactions on mock 'iMethods':",
                        "-> at ",
                        "-> at ",
                        "***",
                        "For your reference, here is the list of all invocations ([?] - means unverified).",
                        "1. [?]-> at ",
                        "2. [?]-> at ");
    }

    @SuppressWarnings({"all", "CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldScreamWhenNullPassed() {
        assertThatThrownBy(
                        () -> {
                            inOrder((Object[]) null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Method requires argument(s)!",
                        "Pass mocks that require verification in order.",
                        "For example:",
                        "    InOrder inOrder = inOrder(mockOne, mockTwo);");
    }

    @Test
    public void shouldThrowNullPassedToVerifyException() {
        try {
            inOrder.verify(null);
            fail();
        } catch (NullInsteadOfMockException e) {
            assertThat(e)
                    .hasMessageContaining(
                            "Argument passed to verify() should be a mock but is null!");
        }
    }

    @Test
    public void shouldThrowNotAMockPassedToVerifyException() {
        Object object = new Object();
        try {
            inOrder.verify(object);
            fail();
        } catch (NotAMockException e) {
            assertThat(e)
                    .hasMessageContaining(
                            "Argument passed to verify() is of type Object and is not a mock!");
        }
    }
}
