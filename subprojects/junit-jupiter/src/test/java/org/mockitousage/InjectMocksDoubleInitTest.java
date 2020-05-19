/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage;

import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class InjectMocksDoubleInitTest {

    @InjectMocks
    Target target;

    @Mock
    Foo foo;

    @Mock
    Bar bar;

    @Test
    public void injectMocks() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(this);

        assertNotSame(foo, target.getFoo());
        assertNotSame(bar, target.getBar());
    }

    public static class Target {
        private final Foo foo;
        private final Bar bar;

        public Target(Foo foo, Bar bar) {
            this.foo = foo;
            this.bar = bar;
        }

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
