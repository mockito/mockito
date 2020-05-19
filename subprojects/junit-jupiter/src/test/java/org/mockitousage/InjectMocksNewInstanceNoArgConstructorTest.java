/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocksNewInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InjectMocksNewInstanceNoArgConstructorTest {

    @InjectMocksNewInstance
    Target target;

    @Mock
    Foo foo;

    @Mock
    Bar bar;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void injectMocks1() {
        assertEquals(foo, target.getFoo());
        assertEquals(bar, target.getBar());
    }

    @Test
    public void injectMocks2() {
        assertEquals(foo, target.getFoo());
        assertEquals(bar, target.getBar());
    }

    public static class Target {
        private Foo foo;
        private Bar bar;

        public Foo getFoo() {
            return foo;
        }

        public Bar getBar() {
            return bar;
        }
    }

    public static class Foo {
    }

    public static class Bar {
    }
}
