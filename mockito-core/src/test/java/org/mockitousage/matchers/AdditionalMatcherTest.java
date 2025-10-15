/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdditionalMatcherTest {

    interface Foo {
        int doSomeThing(Object param);
    }

    @Test
    public void shouldMatchArgumentsSequentiallyWithAnd() {
        Foo foo = mock(Foo.class);

        when(foo.doSomeThing(and(any(String.class), argThat(String::isEmpty)))).thenReturn(1);

        assertNotEquals(foo.doSomeThing(1), 1);
    }

    @Test
    public void shouldMatchArgumentsSequentiallyWithOr() {
        Foo foo = mock(Foo.class);

        when(foo.doSomeThing(
                        or(any(Integer.class), and(any(Object.class), argThat(String::isEmpty)))))
                .thenReturn(1);

        assertEquals(foo.doSomeThing(1), 1);
    }
}
