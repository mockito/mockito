/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.MockedSingleton;
import org.mockito.Mockito;

public final class SingletonMockTest {

    @Test
    public void testSingletonMockSimple() {
        MyEnum singleton = MyEnum.A;
        assertEquals("foo", singleton.foo());
        try (MockedSingleton<MyEnum> ignored = Mockito.mockSingleton(singleton)) {
            assertNull(singleton.foo());
        }
        assertEquals("foo", singleton.foo());
    }

    @Test
    public void testSingletonMockWithStubbing() {
        MyEnum singleton = MyEnum.A;
        assertEquals("foo", singleton.foo());
        try (MockedSingleton<MyEnum> ignored = Mockito.mockSingleton(singleton)) {
            when(singleton.foo()).thenReturn("bar");

            assertEquals("bar", singleton.foo());
        }
    }

    @Test
    public void testSingletonMockDoesNotMockStaticMethods() {
        MyEnum singleton = MyEnum.A;
        try (MockedSingleton<MyEnum> ignored = Mockito.mockSingleton(singleton)) {
            assertEquals("static foo", MyEnum.staticFoo());
        }
    }

    @Test
    public void testSingletonMockGetInstance() {
        MyEnum singleton = MyEnum.A;
        try (MockedSingleton<MyEnum> mockedSingleton = Mockito.mockSingleton(singleton)) {
            assertEquals(singleton, mockedSingleton.getInstance());
        }
    }

    @Test
    public void testSingletonMockEnumDoesNotAffectOtherValues() {
        try (MockedSingleton<MyEnum> ignored = Mockito.mockSingleton(MyEnum.A)) {
            assertEquals("foo", MyEnum.B.foo());
        }
    }

    @Test
    public void testSingletonMockStubOverriddenMethod() {
        MyEnum singleton = MyEnum.A;

        assertEquals("a", singleton.overridden());
        try (MockedSingleton<MyEnum> ignored = Mockito.mockSingleton(singleton)) {
            assertNull(singleton.overridden());
        }
        assertEquals("a", singleton.overridden());
    }

    @Test
    public void testSingletonMockReset() {
        MyEnum singleton = MyEnum.A;

        try (MockedSingleton<MyEnum> ignored = Mockito.mockSingleton(singleton)) {
            when(singleton.foo()).thenReturn("bar");

            reset(singleton);

            assertNull(singleton.foo());
        }
    }

    @Test
    public void testSingletonMockDeregisteredByClearInlineMocks() {
        MyEnum singleton = MyEnum.A;

        // Intentionally do not close the MockedSingleton to simulate a leak.
        Mockito.mockSingleton(singleton);
        when(singleton.foo()).thenReturn("bar");
        assertEquals("bar", singleton.foo());

        Mockito.framework().clearInlineMocks();

        // After clearing, the singleton must no longer be mocked
        assertEquals("foo", singleton.foo());
        // and re-mocking the same instance must not throw "already registered".
        try (MockedSingleton<MyEnum> ignored = Mockito.mockSingleton(singleton)) {
            assertNull(singleton.foo());
        }
    }

    enum MyEnum {
        A {
            @Override
            String overridden() {
                return "a";
            }
        },
        B {
            @Override
            String overridden() {
                return "b";
            }
        },
        ;

        String foo() {
            return "foo";
        }

        abstract String overridden();

        static String staticFoo() {
            return "static foo";
        }
    }
}
