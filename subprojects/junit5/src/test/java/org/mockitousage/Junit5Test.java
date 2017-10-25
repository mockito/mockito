package org.mockitousage;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit5.MockitoExtension;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
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
    @ExtendWith(MockitoExtension.class)
        // ^^ duplicate registartion should be ignored by JUnit
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
        void shouldWeCreateMocksInTheParentContext() {
            assertThat(mock).isNotNull();
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
        // ^^ duplicate registartion should be ignored by JUnit
        // see http://junit.org/junit5/docs/current/user-guide/#extensions-registration-inheritance
    class ParentMockNestedExtension {

        /**
         * Should creation of mock in parents be supported? I would argue -> maybe later...
         */
        @Test
        void shouldWeMocksOfTheParentContext() {
            assertThat(mock).isNotNull();
        }
    }


}
