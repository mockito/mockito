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
    private Function<Integer,String> mock;

    @Test
    void ensureMockCreationWorks() {
        assertThat(mock).isNotNull();
    }

    @Nested
    class NestedMocking {

        @Mock
        Object nestedMock;

        @Test
        void ensureMocksAreCreatedForNestedTests(){

            assertThat(nestedMock).isNotNull();
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    class DuplicateExtensionOnNestedTest {

        @Mock
        Object nestedMock;

        @Test
        void ensureMocksAreCreatedForNestedTests(){
            assertThat(nestedMock).isNotNull();
        }
    }


}
