/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.mockito.Mockito.spy;

public class RecursionTest {

    @Test
    public void testMockConcurrentHashMap() {
        ConcurrentMap<String, String> map = spy(new ConcurrentHashMap<String, String>());
        map.putIfAbsent("a", "b");
    }
}
