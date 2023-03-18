/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.TestCase.*;

public final class ConstructionMockRuleTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MockedConstruction<Dummy> dummy;

    @Test
    public void testConstructionMockSimple() {
        assertNull(new Dummy().foo());
    }

    @Test
    public void testConstructionMockCollection() {
        assertEquals(0, dummy.constructed().size());
        Dummy mock = new Dummy();
        assertEquals(1, dummy.constructed().size());
        assertTrue(dummy.constructed().contains(mock));
    }

    static class Dummy {

        String foo() {
            return "foo";
        }
    }
}
