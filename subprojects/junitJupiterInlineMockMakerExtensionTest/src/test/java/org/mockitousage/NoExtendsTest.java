/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.*;

class NoExtendsTest {

    @Mock
    private MockedStatic<UUID> mock;

    @Test
    void runs() {
        mock.when(UUID::randomUUID).thenReturn(new UUID(123, 456));
        assertThat(UUID.randomUUID()).isEqualTo(new UUID(123, 456));
    }
}
