/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class PlaygroundTest extends TestBase {

    private static class BaseClass {
        private final String s;

        public BaseClass(String s) {
            s.getClass(); //NPE check
            this.s = s;
        }

        public boolean isLarger(int i) {
            return s.length() < i;
        }
    }

    /**
     * This test succeeds.
     */
    @Test
    public void testSpyOfBaseClass() {
        BaseClass ci = spy(new BaseClass("test"));
        assertEquals(false, ci.isLarger(2));
    }

    private static class Subclass extends BaseClass {
        public Subclass(String s) {
            super(s);
        }

        public boolean isLarger(int i) {
            return super.isLarger(i);
        }
    }

    /**
     * This test fails with a NullPointerException.
     */
    @Test
    public void testSpyOfSubclass() {
        BaseClass ci = spy(new Subclass("test"));
        assertEquals(false, ci.isLarger(2));
    }
}