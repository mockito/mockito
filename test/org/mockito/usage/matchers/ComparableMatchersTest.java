/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage.matchers;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.mockito.internal.matchers.*;

//TODO do we need matchers like GreaterThan - does it make any sense?
public class ComparableMatchersTest {

    @Test
    public void testNotComparable() {
        CompareTo<Long> cmpTo = new CompareTo<Long>(5L) {

            @Override
            protected String getName() {
                return null;
            }

            @Override
            protected boolean matchResult(int result) {
                fail("Shouldn't be called since the passed argument is not Comparable");
                return true;
            }
            
        };
        
        assertFalse(cmpTo.matches(new Object()));
    }
    @Test
    public void testLessThan() {
        test(new LessThan<String>("b"), true, false, false, "lt");
    }

    @Test
    public void testGreateThan() {
        test(new GreaterThan<String>("b"), false, true, false, "gt");
    }

    @Test
    public void testLessOrEqual() {
        test(new LessOrEqual<String>("b"), true, false, true, "leq");
    }

    @Test
    public void testGreateOrEqual() {
        test(new GreaterOrEqual<String>("b"), false, true, true, "geq");
    }

    @Test
    public void testCompareEqual() {
        test(new CompareEqual<String>("b"), false, false, true, "cmpEq");

        // Make sure it works when equals provide a different result than
        // compare
        CompareEqual<BigDecimal> cmpEq = new CompareEqual<BigDecimal>(
                new BigDecimal("5.00"));
        assertTrue(cmpEq.matches(new BigDecimal("5")));
    }

    private void test(CompareTo<String> compareTo, boolean lower, boolean higher,
            boolean equals, String name) {

        assertEquals(lower, compareTo.matches("a"));
        assertEquals(equals, compareTo.matches("b"));
        assertEquals(higher, compareTo.matches("c"));

        StringBuffer sb = new StringBuffer();
        compareTo.appendTo(sb);
        assertEquals(name + "(b)", sb.toString());
    }
}
