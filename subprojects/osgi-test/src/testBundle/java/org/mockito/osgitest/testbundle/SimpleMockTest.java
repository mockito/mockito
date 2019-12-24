/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleMockTest {

    @Test
    public void test() {
        List list = mock(List.class);
        when(list.get(0)).thenReturn("a");
        assertEquals("a", list.get(0));
    }
}
