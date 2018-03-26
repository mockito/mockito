/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs.varargs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

//see issue 62
public class VarargsNotPlayingWithAnyObjectTest extends TestBase {

    interface VarargMethod {
        Object run(String... args);
    }

    @Mock VarargMethod mock;

    @Test
    public void shouldMatchAnyVararg() {
        mock.run("a", "b");

        verify(mock).run(anyString(), anyString());
        verify(mock).run((String) anyObject(), (String) anyObject());

        verify(mock).run((String[]) anyVararg());

        verify(mock, never()).run();
        verify(mock, never()).run(anyString(), eq("f"));
    }

    @Test
    public void shouldStubUsingAnyVarargs() {
        when(mock.run((String[]) anyVararg())).thenReturn("foo");

        assertEquals("foo", mock.run("a", "b"));
    }
}
