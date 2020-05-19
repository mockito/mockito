/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocksNewInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class I {}

class InjectMocksNewInstanceHierarchyTest extends I {

    @InjectMocksNewInstance
    Target target;

    @Mock
    Foo foo;

    @Spy
    Bar bar = new Bar();

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void injectMocks() {
        assertEquals(foo, target.getFoo());
        assertNull(target.getBar());
    }

    public static class Target {
        private final Foo foo;
        private Bar bar;

        public Target(Foo foo) {
            this.foo = foo;
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
