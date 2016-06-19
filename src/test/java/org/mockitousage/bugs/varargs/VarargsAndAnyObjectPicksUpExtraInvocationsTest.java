/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs.varargs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VarargsAndAnyObjectPicksUpExtraInvocationsTest extends TestBase {
    public interface TableBuilder {
        void newRow(String trAttributes, String... cells);
    }

    @Mock
    TableBuilder table;

    @Test
    public void shouldVerifyCorrectlyWithAnyVarargs() {
        //when
        table.newRow("qux", "foo", "bar", "baz");
        table.newRow("abc", "def");
        
        //then
        verify(table, times(2)).newRow(anyString(), (String[]) anyVararg());
    }

    @Test
    public void shouldVerifyCorrectlyNumberOfInvocationsUsingAnyVarargAndEqualArgument() {
        //when
        table.newRow("x", "foo", "bar", "baz");
        table.newRow("x", "def");

        //then
        verify(table, times(2)).newRow(eq("x"), (String[]) anyVararg());
    }

    @Test
    public void shouldVerifyCorrectlyNumberOfInvocationsWithVarargs() {
        //when
        table.newRow("qux", "foo", "bar", "baz");
        table.newRow("abc", "def");
        
        //then
        verify(table).newRow(anyString(), eq("foo"), anyString(), anyString());
        verify(table).newRow(anyString(), anyString());
    }
}