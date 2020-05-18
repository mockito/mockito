/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.validateMockitoUsage;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class MisusingStubbingTest extends TestBase {

    @Test
    public void clean_state_after_not_a_mock() {
        // when
        assertThatThrownBy(() -> doReturn(100).when("not a mock"));

        // then
        validateMockitoUsage();
    }

    @Test
    public void clean_state_after_null_passed() {
        // when
        assertThatThrownBy(() -> doReturn(100).when(null));

        // then
        validateMockitoUsage();
    }
}
