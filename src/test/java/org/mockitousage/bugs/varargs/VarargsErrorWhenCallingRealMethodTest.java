/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs.varargs;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class VarargsErrorWhenCallingRealMethodTest extends TestBase {

    class Foo {
        int blah(String a, String b, Object ... c) {
            return 1;
        }
    }

    @Test
    public void shouldNotThrowAnyException() throws Exception {
        Foo foo = mock(Foo.class);

        when(foo.blah(anyString(), anyString())).thenCallRealMethod();

        assertEquals(1, foo.blah("foo", "bar"));
    }
}
