/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;
import org.objenesis.ObjenesisException;

// issue 1514
@SuppressWarnings({"serial", "unchecked", "rawtypes"})
public class StubbingWithBadThrowablesTest extends TestBase {

    @Mock List mock;

    @Test
    public void handles_bad_exception() {
        Assertions.assertThatThrownBy(
                        new ThrowableAssert.ThrowingCallable() {
                            public void call() {
                                doThrow(UninstantiableException.class).when(mock).clear();
                            }
                        })
                .satisfiesAnyOf(
                        throwable ->
                                Assertions.assertThat(throwable)
                                        .isInstanceOf(InstantiationError.class),
                        // Java 25 wraps the failed reflective instantiation in ObjenesisException.
                        throwable ->
                                Assertions.assertThat(throwable)
                                        .isInstanceOf(ObjenesisException.class)
                                        .hasRootCauseInstanceOf(InstantiationException.class));

        // ensure that the state is cleaned
        Mockito.validateMockitoUsage();
    }

    abstract static class UninstantiableException extends RuntimeException {}
}
