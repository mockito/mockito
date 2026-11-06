/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {
    @Mock private VerificationMode delegate;
    private VerificationOverTimeImpl impl;

    @Before
    public void setUp() {
        openMocks(this);
        impl = new VerificationOverTimeImpl(10, 1000, delegate, true);
    }

    @Test
    public void should_return_on_success() {
        impl.verify(null);
        verify(delegate).verify(null);
    }

    @Test
    public void should_sleep_between_successful_polls_when_not_returning_on_success() {
        VerificationOverTimeImpl afterImpl = new VerificationOverTimeImpl(50, 200, delegate, false);

        long start = System.currentTimeMillis();
        afterImpl.verify(null);
        long elapsed = System.currentTimeMillis() - start;

        assertThat(elapsed).isGreaterThanOrEqualTo(200);
        verify(delegate, atLeast(2)).verify(null);
        verify(delegate, atMost(20)).verify(null);
    }

    @Test
    public void should_throw_mockito_assertion_error() {
        MockitoAssertionError toBeThrown = new MockitoAssertionError("message");

        doThrow(toBeThrown).when(delegate).verify(null);
        assertThatThrownBy(
                        () -> {
                            impl.verify(null);
                        })
                .isEqualTo(toBeThrown);
    }

    @Test
    public void should_deal_with_junit_assertion_error() {
        ArgumentsAreDifferent toBeThrown = new ArgumentsAreDifferent("message", "wanted", "actual");

        doThrow(toBeThrown).when(delegate).verify(null);
        assertThatThrownBy(
                        () -> {
                            impl.verify(null);
                        })
                .isEqualTo(toBeThrown);
    }

    @Test
    public void should_not_wrap_other_exceptions() {
        RuntimeException toBeThrown = new RuntimeException();

        doThrow(toBeThrown).when(delegate).verify(null);
        assertThatThrownBy(
                        () -> {
                            impl.verify(null);
                        })
                .isEqualTo(toBeThrown);
    }
}
