/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs.varargs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyVararg;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void shouldAllowUsingAnyObjectForVarArgs() {
        mock.run("a", "b");
        verify(mock).run((String[]) anyObject());
    }

    @Test
    public void shouldStubUsingAnyVarargs() {
        when(mock.run((String[]) anyVararg())).thenReturn("foo");

        assertEquals("foo", mock.run("a", "b"));
    }
}
