/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class VarargsErrorWhenCallingRealMethodTest extends TestBase {

    class Foo {
        int blah(final String a, final String b, final Object ... c) {
            return 1;
        }
    }

    @Test
    public void shouldNotThrowAnyException() throws Exception {
        final Foo foo = mock(Foo.class);

        when(foo.blah(anyString(), anyString())).thenCallRealMethod();

        assertEquals(1, foo.blah("foo", "bar"));
    }
}