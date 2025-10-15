/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class VerificationInOrderWithCallsTest extends TestBase {

    @Mock private IMethods mockOne;
    @Mock private IMethods mockTwo;

    @Test
    public void shouldFailWhenMethodNotCalled() {
        // Given
        mockOne.oneArg(1);
        InOrder verifier = inOrder(mockOne);
        verifier.verify(mockOne, calls(1)).oneArg(1);

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verify(mockOne, calls(1)).oneArg(2);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "Verification in order failure",
                        "Wanted but not invoked",
                        "mockOne.oneArg(2)");
    }

    @Test
    public void shouldFailWhenMethodCalledTooFewTimes() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);

        InOrder verifier = inOrder(mockOne);
        verifier.verify(mockOne, calls(1)).oneArg(1);

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verify(mockOne, calls(2)).oneArg(2);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "Verification in order failure",
                        "mockOne.oneArg(2)",
                        "Wanted 2 times",
                        "But was 1 time");
    }

    @Test
    public void shouldFailWhenSingleMethodCallsAreOutOfSequence() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);

        InOrder verifier = inOrder(mockOne);
        verifier.verify(mockOne, calls(1)).oneArg(2);

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verify(mockOne, calls(1)).oneArg(1);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "Verification in order failure",
                        "Wanted but not invoked",
                        "mockOne.oneArg(1)");
    }

    @Test
    public void shouldFailWhenDifferentMethodCallsAreOutOfSequence() {
        // Given
        mockOne.oneArg(1);
        mockOne.voidMethod();

        InOrder verifier = inOrder(mockOne);
        verifier.verify(mockOne, calls(1)).voidMethod();

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verify(mockOne, calls(1)).oneArg(1);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "Verification in order failure",
                        "Wanted but not invoked",
                        "mockOne.oneArg(1)");
    }

    @Test
    public void shouldFailWhenMethodCallsOnDifferentMocksAreOutOfSequence() {
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();

        InOrder verifier = inOrder(mockOne, mockTwo);
        verifier.verify(mockTwo, calls(1)).voidMethod();

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verify(mockOne, calls(1)).voidMethod();
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "Verification in order failure",
                        "Wanted but not invoked",
                        "mockOne.voidMethod()");
    }

    @Test
    public void shouldAllowSequentialCallsToCallsForSingleMethod() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);
        mockOne.oneArg(2);
        mockOne.oneArg(1);

        InOrder verifier = inOrder(mockOne);

        // When
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(2)).oneArg(2);
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifyNoMoreInteractions(mockOne);
        verifier.verifyNoMoreInteractions();

        // Then - no exception thrown
    }

    @Test
    public void shouldAllowSequentialCallsToCallsForDifferentMethods() {
        // Given
        mockOne.oneArg(1);
        mockOne.voidMethod();
        mockOne.voidMethod();
        mockOne.oneArg(1);

        InOrder verifier = inOrder(mockOne);

        // When
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(2)).voidMethod();
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifyNoMoreInteractions(mockOne);
        verifier.verifyNoMoreInteractions();

        // Then - no exception thrown
    }

    @Test
    public void shouldAllowSequentialCallsToCallsForMethodsOnDifferentMocks() {
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();
        mockOne.voidMethod();

        InOrder verifier = inOrder(mockOne, mockTwo);

        // When
        verifier.verify(mockOne, calls(1)).voidMethod();
        verifier.verify(mockTwo, calls(2)).voidMethod();
        verifier.verify(mockOne, calls(1)).voidMethod();
        verifyNoMoreInteractions(mockOne);
        verifyNoMoreInteractions(mockTwo);
        verifier.verifyNoMoreInteractions();

        // Then - no exception thrown
    }

    @Test
    public void shouldAllowFewerCallsForSingleMethod() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);
        mockOne.oneArg(2);
        mockOne.oneArg(1);
        mockOne.oneArg(2);

        InOrder verifier = inOrder(mockOne);

        // When
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(1)).oneArg(2);
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(1)).oneArg(2);

        // Then - no exception thrown
    }

    @Test
    public void shouldNotVerifySkippedCallsWhenFewerCallsForSingleMethod() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);
        mockOne.oneArg(2);
        mockOne.oneArg(1);

        InOrder verifier = inOrder(mockOne);
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(1)).oneArg(2);
        verifier.verify(mockOne, calls(1)).oneArg(1);

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifyNoMoreInteractions(mockOne);
                        })
                .isInstanceOf(NoInteractionsWanted.class);
    }

    @Test
    public void shouldNotVerifySkippedCallsInInOrderWhenFewerCallsForSingleMethod() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);
        mockOne.oneArg(2);

        InOrder verifier = inOrder(mockOne);
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(1)).oneArg(2);

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verifyNoMoreInteractions();
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContaining("No interactions wanted here");
    }

    @Test
    public void shouldAllowFewerCallsForDifferentMethods() {
        // Given
        mockOne.oneArg(1);
        mockOne.voidMethod();
        mockOne.voidMethod();
        mockOne.oneArg(1);
        mockOne.voidMethod();

        InOrder verifier = inOrder(mockOne);

        // When
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(1)).voidMethod();
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(1)).voidMethod();

        // Then - no exception thrown
    }

    @Test
    public void shouldNotVerifySkippedCallsWhenFewerCallsForDifferentMethods() {
        // Given
        mockOne.oneArg(1);
        mockOne.voidMethod();
        mockOne.voidMethod();
        mockOne.oneArg(1);

        InOrder verifier = inOrder(mockOne);
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(1)).voidMethod();
        verifier.verify(mockOne, calls(1)).oneArg(1);

        // When / Then - no exception thrown
        assertThatThrownBy(
                        () -> {
                            verifyNoMoreInteractions(mockOne);
                        })
                .isInstanceOf(NoInteractionsWanted.class);
    }

    @Test
    public void shouldNotVerifySkippedCallsInInOrderWhenFewerCallsForDifferentMethods() {
        // Given
        mockOne.oneArg(1);
        mockOne.voidMethod();
        mockOne.voidMethod();

        InOrder verifier = inOrder(mockOne);
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, calls(1)).voidMethod();

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verifyNoMoreInteractions();
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContaining("No interactions wanted here");
    }

    @Test
    public void shouldAllowFewerCallsForMethodsOnDifferentMocks() {
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();
        mockOne.voidMethod();
        mockTwo.voidMethod();

        InOrder verifier = inOrder(mockOne, mockTwo);

        // When
        verifier.verify(mockOne, calls(1)).voidMethod();
        verifier.verify(mockTwo, calls(1)).voidMethod();
        verifier.verify(mockOne, calls(1)).voidMethod();
        verifier.verify(mockTwo, calls(1)).voidMethod();

        // Then - no exception thrown
    }

    @Test
    public void shouldNotVerifySkippedCallsWhenFewerCallsForMethodsOnDifferentMocks() {
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();
        mockOne.voidMethod();

        InOrder verifier = inOrder(mockOne, mockTwo);
        verifier.verify(mockOne, calls(1)).voidMethod();
        verifier.verify(mockTwo, calls(1)).voidMethod();
        verifier.verify(mockOne, calls(1)).voidMethod();

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifyNoMoreInteractions(mockTwo);
                        })
                .isInstanceOf(NoInteractionsWanted.class);
    }

    @Test
    public void shouldNotVerifySkippedCallsInInOrderWhenFewerCallsForMethodsOnDifferentMocks() {
        // Given
        mockOne.voidMethod();
        mockTwo.voidMethod();
        mockTwo.voidMethod();

        InOrder verifier = inOrder(mockOne, mockTwo);
        verifier.verify(mockOne, calls(1)).voidMethod();
        verifier.verify(mockTwo, calls(1)).voidMethod();

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verifyNoMoreInteractions();
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContaining("No interactions wanted here");
    }

    @Test
    public void shouldVerifyWithCallsAfterUseOfTimes() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);
        mockOne.oneArg(2);
        mockOne.oneArg(1);

        InOrder verifier = inOrder(mockOne);

        // When
        verifier.verify(mockOne, times(1)).oneArg(1);
        verifier.verify(mockOne, calls(2)).oneArg(2);
        verifier.verify(mockOne, calls(1)).oneArg(1);

        // Then - no exception thrown
    }

    @Test
    public void shouldVerifyWithCallsAfterUseOfAtLeast() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);
        mockOne.oneArg(2);

        InOrder verifier = inOrder(mockOne);

        // When
        verifier.verify(mockOne, atLeast(1)).oneArg(1);
        verifier.verify(mockOne, calls(2)).oneArg(2);

        // Then - no exception thrown
    }

    @Test
    public void shouldVerifyWithTimesAfterUseOfCalls() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);
        mockOne.oneArg(2);
        mockOne.oneArg(1);

        InOrder verifier = inOrder(mockOne);

        // When
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, times(2)).oneArg(2);
        verifier.verify(mockOne, times(1)).oneArg(1);

        // Then - no exception thrown
    }

    @Test
    public void shouldVerifyWithAtLeastAfterUseOfCalls() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(2);
        mockOne.oneArg(2);
        mockOne.oneArg(1);

        InOrder verifier = inOrder(mockOne);

        // When
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, atLeast(1)).oneArg(2);
        verifier.verify(mockOne, atLeast(1)).oneArg(1);

        // Then - no exception thrown
    }

    @Test
    public void shouldVerifyWithTimesAfterCallsInSameChunk() {
        // Given
        mockOne.oneArg(1);
        mockOne.oneArg(1);
        mockOne.oneArg(1);

        InOrder verifier = inOrder(mockOne);

        // When
        verifier.verify(mockOne, calls(1)).oneArg(1);
        verifier.verify(mockOne, times(2)).oneArg(1);
        verifier.verifyNoMoreInteractions();

        // Then - no exception thrown
    }

    @Test
    public void shouldFailToCreateCallsWithZeroArgument() {
        // Given
        InOrder verifier = inOrder(mockOne);

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verify(mockOne, calls(0)).voidMethod();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Negative and zero values are not allowed here");
    }

    @Test
    public void shouldFailToCreateCallsWithNegativeArgument() {
        // Given
        InOrder verifier = inOrder(mockOne);

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verifier.verify(mockOne, calls(-1)).voidMethod();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Negative and zero values are not allowed here");
    }

    @Test
    public void shouldFailToCreateCallsForNonInOrderVerification() {
        // Given
        mockOne.voidMethod();

        // When / Then - expected exception thrown
        assertThatThrownBy(
                        () -> {
                            verify(mockOne, calls(1)).voidMethod();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("calls is only intended to work with InOrder");
    }
}
