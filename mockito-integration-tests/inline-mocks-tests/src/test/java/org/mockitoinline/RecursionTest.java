/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;
import org.mockito.MockedSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockSingleton;
import static org.mockito.Mockito.spy;

public class RecursionTest {

    @Test
    public void testMockConcurrentHashMap() {
        ConcurrentMap<String, String> map = spy(new ConcurrentHashMap<String, String>());
        map.putIfAbsent("a", "b");
    }

    enum MyEnum {
        A
    }

    @Test
    public void testSingletonMockAndInstrumentingAbstractList() {
        // Initializes mockedSingletons map
        try (MockedSingleton<MyEnum> ignored = mockSingleton(MyEnum.A)) {}
        // instruments AbstractList whose hashCode() implementation invokes the instrumented method
        // iterator()
        List<?> listMock = mock(ArrayList.class);
        // Verify no StackOverflowError when invoking method on instrumented class
        listMock.clear();
    }
}
