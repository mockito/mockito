/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnstubbedInvocation;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@MockitoSettings(strictness = Strictness.STRICT_MOCKS)
class StrictMocksJUnitExtensionTest {

    @Mock List mock;

    @Test
    void unstubbed_invocation_is_detected() {
        assertThatExceptionOfType(UnstubbedInvocation.class).isThrownBy(() -> mock.get(1));
    }
}
