/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers.foopackage;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;

public class ForwardsInvocationsTest extends TestBase {

    interface Foo {
        int bar();
    }

    @Test
    public void should_call_anonymous_class_method() throws Throwable {
        ForwardsInvocations forwardsInvocations = new ForwardsInvocations(new Foo() {
            @Override
            public int bar() {
                return 0;
            }
        });
        assertEquals(0, forwardsInvocations.answer(invocationOf(Foo.class, "bar")));
    }
}
