package org.mockitousage;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit5.WithMockito;

import static org.assertj.core.api.Assertions.assertThat;

@WithMockito
public class NestedTests {
    @Mock Runnable rootMock;

    @Test
    void rootMockCreated() {
        assertThat(rootMock).isNotNull();
    }

    @Nested
    class NestedTest {
        @Mock Runnable nestedMock;

        @Test
        void nestedMockCreated() {
            assertThat(nestedMock).isNotNull();
        }

        @Test
        void rootMockCreated() {
            assertThat(rootMock).isNotNull();
        }
    }
}
