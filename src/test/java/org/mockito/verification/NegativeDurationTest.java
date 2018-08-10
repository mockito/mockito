/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.InvalidArgumentException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class NegativeDurationTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void should_throw_exception_when_duration_is_negative_for_timeout_method() {
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                verify(mock, timeout(-1)).simpleMethod();
            }
        }).isInstanceOf(InvalidArgumentException.class);
    }

    @Test
    public void should_throw_exception_when_duration_is_negative_for_after_method() {
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            public void call() {
                verify(mock, after(-1)).simpleMethod();
            }
        }).isInstanceOf(InvalidArgumentException.class);
    }
}
