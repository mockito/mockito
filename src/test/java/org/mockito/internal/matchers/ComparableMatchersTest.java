/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockitoutil.TestBase;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComparableMatchersTest extends TestBase {

    @Test
    public void testLessThan() {
        test(new LessThan<String>("b"), true, false, false, "lt");
    }

    @Test
    public void testGreaterThan() {
        test(new GreaterThan<String>("b"), false, true, false, "gt");
    }

    @Test
    public void testLessOrEqual() {
        test(new LessOrEqual<String>("b"), true, false, true, "leq");
    }

    @Test
    public void testGreaterOrEqual() {
        test(new GreaterOrEqual<String>("b"), false, true, true, "geq");
    }

    @Test
    public void testCompareEqual() {
        test(new CompareEqual<String>("b"), false, false, true, "cmpEq");

        // Make sure it works when equals provide a different result than compare
        CompareEqual<BigDecimal> cmpEq = new CompareEqual<BigDecimal>(new BigDecimal("5.00"));
        assertTrue(cmpEq.matches(new BigDecimal("5")));
    }

    private void test(CompareTo<String> compareTo, boolean lower, boolean higher,
            boolean equals, String name) {

        assertEquals(lower, compareTo.matches("a"));
        assertEquals(equals, compareTo.matches("b"));
        assertEquals(higher, compareTo.matches("c"));

        assertEquals(name + "(b)", compareTo.toString());
    }
}
