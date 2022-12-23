/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.varargs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

// see issue 62
public class VarargsNotPlayingWithAnyTest extends TestBase {

    interface VarargMethod {
        Object run(String... args);
    }

    @Mock VarargMethod mock;

    @Test
    public void shouldMatchAny() {
        mock.run("a", "b");

        verify(mock).run(anyString(), anyString());
        verify(mock).run(any(), any());

        verify(mock).run(any(String[].class));

        verify(mock, never()).run();
        verify(mock, never()).run(anyString(), eq("f"));
    }

    @Test
    public void shouldAllowUsingAnyForVarArgs() {
        mock.run("a", "b");
        verify(mock).run(any(String[].class));
    }

    @Test
    public void shouldStubUsingAny() {
        when(mock.run(any(String[].class))).thenReturn("foo");

        assertEquals("foo", mock.run("a", "b"));
    }
}
