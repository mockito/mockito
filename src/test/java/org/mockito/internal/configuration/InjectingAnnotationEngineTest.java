/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

class I {}

@RunWith(MockitoJUnitRunner.class)
public class InjectingAnnotationEngineTest extends I {
    @InjectMocks Target target;
    @Mock Foo foo;
    @Spy Bar bar = new Bar();

    /*
     If the test case has super classes, the @InjectMocks field has a field that not listed in the constructor argument
     will fill by setter/property injection .

     https://github.com/mockito/mockito/issues/1631
    */
    @Test
    public void injectMocks() {
        Assert.assertEquals(foo, target.getFoo());
        Assert.assertNotNull(target.getBar());
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

    public static class Foo {}

    public static class Bar {}
}
