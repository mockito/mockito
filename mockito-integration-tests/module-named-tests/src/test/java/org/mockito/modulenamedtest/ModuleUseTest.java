/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.modulenamedtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ModuleUseTest {

    @Test
    public void can_mock_with_named_module() {
        Foo foo = mock(Foo.class);
        when(foo.value()).thenReturn("foo");
        assertThat(foo.value()).isEqualTo("foo");
    }

    @Test
    public void can_mock_with_named_module_subclass() {
        Bar bar = mock(Bar.class);
        when(bar.value()).thenReturn("bar");
        assertThat(bar.value()).isEqualTo("bar");
    }

    public static class Foo {

        public String value() {
            return null;
        }
    }

    public abstract static class Bar {

        public String value() {
            return null;
        }
    }
}
