/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.regression;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * <a href="https://github.com/mockito/mockito/issues/3012">Issue #3012</a>
 */
@ExtendWith(MockitoExtension.class)
public class Regression3112Test {

    @Mock private InterfaceA<?> mock;

    @InjectMocks private ClassB<?> b;
    @InjectMocks private ClassC<?> c;
    @InjectMocks private ClassB<Object> d;

    @Test
    void testSuccessfullyInjected() {
        assertSame(mock, b.member);
        assertSame(mock, c.member);
        assertSame(mock, d.member);
    }

    @SuppressWarnings("unused")
    private interface InterfaceA<R> {}

    private static class ClassA<R> {
        InterfaceA<R> member;
    }

    private static class ClassB<R> extends ClassA<R> {}

    private static class ClassC<R> extends ClassA<Void> {}
}
