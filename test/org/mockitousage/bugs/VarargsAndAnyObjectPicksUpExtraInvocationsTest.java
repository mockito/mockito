/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

public class VarargsAndAnyObjectPicksUpExtraInvocationsTest extends TestBase {
    public interface TableBuilder {
        void newRow(String trAttributes, String... cells);
    }

    @Mock
    TableBuilder table;

    //TODO: after 1.8 - it's a minor bug but not very easy to fix.
    @Ignore
    @Test
    public void shouldVerifyCorrectlyWithAnyObjectSubstitutingVarargs() {
        //when
        table.newRow("qux", "foo", "bar", "baz");
        table.newRow("abc", "def");
        
        //then
        verify(table, times(2)).newRow(anyString(), (String[]) anyObject());
    }

    @Test
    public void shouldVerifyCorrectlyWithVarargs() {
        //when
        table.newRow("qux", "foo", "bar", "baz");
        table.newRow("abc", "def");
        
        //then
        verify(table).newRow(anyString(), eq("foo"), anyString(), anyString());
        verify(table).newRow(anyString(), anyString());
    }
}