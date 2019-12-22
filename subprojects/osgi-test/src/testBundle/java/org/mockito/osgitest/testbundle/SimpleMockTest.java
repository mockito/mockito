/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleMockTest implements Runnable {

    @Override
    public void run() {
        List list = mock(List.class);
        when(list.get(0)).thenReturn("a");
        if (!list.get(0).equals("a")) {
            throw new AssertionError();
        }
    }
}
