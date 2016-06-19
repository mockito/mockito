/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.internal.matchers.Equality.areEqual;

public class EqualityTest extends TestBase {
    
    @Test
    public void shouldKnowIfObjectsAreEqual() throws Exception {
        int[] arr = new int[] {1, 2};
        assertTrue(areEqual(arr, arr));
        assertTrue(areEqual(new int[] {1, 2}, new int[] {1, 2}));
        assertTrue(areEqual(new Double[] {1.0}, new Double[] {1.0}));
        assertTrue(areEqual(new String[0], new String[0]));
        assertTrue(areEqual(new Object[10], new Object[10]));
        assertTrue(areEqual(new int[] {1}, new Integer[] {1}));
        assertTrue(areEqual(new Object[] {"1"}, new String[] {"1"}));
        Object badequals=new BadEquals();
        assertTrue(areEqual(badequals,badequals));

        assertFalse(areEqual(new Object[9], new Object[10]));
        assertFalse(areEqual(new int[] {1, 2}, new int[] {1}));
        assertFalse(areEqual(new int[] {1}, new double[] {1.0}));
    }

    private final class BadEquals {
        @Override
        public boolean equals(Object oth) {
            throw new RuntimeException();
        }
    }
}
