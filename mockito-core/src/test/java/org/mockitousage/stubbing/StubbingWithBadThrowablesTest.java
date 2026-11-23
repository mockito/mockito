/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

// issue 1514
@SuppressWarnings({"serial", "unchecked", "rawtypes"})
public class StubbingWithBadThrowablesTest extends TestBase {

    @Mock List mock;

    @Test
    public void handles_bad_exception() {
        assertThatThrownBy(() -> doThrow(UninstantiableException.class).when(mock).clear())
                .hasRootCauseInstanceOf(InstantiationException.class);

        // ensure that the state is cleaned
        Mockito.validateMockitoUsage();
    }

    abstract static class UninstantiableException extends RuntimeException {}
}
