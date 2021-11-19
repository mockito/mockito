/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class ForwardsInvocationsTest extends TestBase {

    interface Foo {
        int bar(String baz, Object... args);
    }

    private static final class FooImpl implements Foo {
        @Override
        public int bar(String baz, Object... args) {
            return baz.length() + args.length;
        }
    }

    @Test
    public void should_call_method_with_varargs() throws Throwable {
        ForwardsInvocations forwardsInvocations = new ForwardsInvocations(new FooImpl());
        assertEquals(
                4,
                forwardsInvocations.answer(
                        invocationOf(Foo.class, "bar", "b", new Object[] {12, "3", 4.5})));
    }

    @Test
    public void should_call_method_with_empty_varargs() throws Throwable {
        ForwardsInvocations forwardsInvocations = new ForwardsInvocations(new FooImpl());
        assertEquals(
                1,
                forwardsInvocations.answer(invocationOf(Foo.class, "bar", "b", new Object[] {})));
    }
}
