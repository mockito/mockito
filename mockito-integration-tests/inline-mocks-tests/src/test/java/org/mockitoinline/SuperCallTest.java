/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public final class SuperCallTest {

    @Test
    public void testSuperMethodCall() {
        Dummy d = spy(new Dummy());
        d.foo();
        verify(d).bar(eq("baz"));
    }

    static class Dummy {

        public void foo() {
            bar("baz");
        }

        // Also fails if public.
        void bar(String s) {
            return;
        }
    }
}
