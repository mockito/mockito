/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MultiLevelNestedTest {

    @Mock
    private Runnable level1Mock;

    @Nested
    class Level2Class {
        @Mock Runnable level2Mock;

        @Test
        void mocks_created() {
            assertThat(level1Mock).isNotNull();
            assertThat(level2Mock).isNotNull();
        }

        @Nested
        class Level3Class {
            @Mock Runnable level3Mock;

            @Test
            void mocks_created() {
                assertThat(level1Mock).isNotNull();
                assertThat(level2Mock).isNotNull();
                assertThat(level3Mock).isNotNull();
            }
        }
    }
}
