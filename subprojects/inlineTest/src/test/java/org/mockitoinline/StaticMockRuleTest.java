/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.TestCase.*;

public final class StaticMockRuleTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MockedStatic<Dummy> dummy;

    @Test
    public void testStaticMockSimple() {
        assertNull(Dummy.foo());
    }

    @Test
    public void testStaticMockWithVerification() {
        dummy.when(Dummy::foo).thenReturn("bar");
        assertEquals("bar", Dummy.foo());
        dummy.verify(Dummy::foo);
    }

    static class Dummy {

        static String foo() {
            return "foo";
        }
    }
}
