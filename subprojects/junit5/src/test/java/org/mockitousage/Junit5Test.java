/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit5.WithMockito;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@WithMockito
class Junit5Test {

    @Mock
    private Function<Integer, String> mock;

    @Test
    void ensureMockCreationWorks() {
        assertThat(mock).isNotNull();
    }

    @Nested
    class NestedMocking {

        @Mock
        Object nestedMock;

        @Test
        void ensureMocksAreCreatedForNestedTests() {
            assertThat(nestedMock).isNotNull();
        }
    }

    @Nested
    @WithMockito
        // ^^ duplicate registration should be ignored by JUnit
        // see http://junit.org/junit5/docs/current/user-guide/#extensions-registration-inherita
    class DuplicateExtensionOnNestedTest {

        @Mock
        Object nestedMock;

        @Test
        void ensureMocksAreCreatedForNestedTests() {
            assertThat(nestedMock).isNotNull();
        }
    }

    @Nested
    class ParentMock {
        @Test
        // mock is not initialized by mockito session
        void shouldWeCreateMocksInTheParentContext() {
            assertThat(mock).isNotNull();
        }
    }
}
