/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class CaptorAnnotationWithPrimitiveTest {
    @Mock private Foo foo;

    static class Foo {
        void doSomething(int value) {}
    }

    @Test
    public void shouldCaptorPrimitive(@Captor ArgumentCaptor<Integer> captor) {
        // Given
        int value = 1;

        // When
        doNothing().when(foo).doSomething(captor.capture());

        // Then
        foo.doSomething(value);
        assertEquals(1, captor.getValue());
    }
}
