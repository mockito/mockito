/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.resolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
class MockResolverTest {

    @Test
    void mock_resolver_can_unwrap_mocked_instance() {
        Foo mock = mock(Foo.class), wrapper = new MockWrapper(mock);
        when(wrapper.doIt()).thenReturn(123);
        assertThat(mock.doIt()).isEqualTo(123);
        assertThat(wrapper.doIt()).isEqualTo(123);
        verify(wrapper, times(2)).doIt();
    }

    interface Foo {
        int doIt();
    }

    static class MockWrapper implements Foo {

        private final Foo mock;

        MockWrapper(Foo mock) {
            this.mock = mock;
        }

        Object getMock() {
            return mock;
        }

        @Override
        public int doIt() {
            return mock.doIt();
        }
    }

}
