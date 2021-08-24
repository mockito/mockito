/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.spies;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class SpyCreationShouldNotAffectMockTest extends TestBase {

    @Test
    public void test() {
        TestClass instance = new TestClass(Long.MIN_VALUE);

        TestClass spy = spy(instance);
        assertEquals(instance.hashCode(), spy.hashCode());
        assertEquals(spy, instance);
        assertEquals(instance, spy);

        TestClass mock = mock(TestClass.class);
        assertEquals(mock.hashCode(), mock.hashCode());
        assertEquals(mock, mock);
    }

    static class TestClass {
        private final long value;

        TestClass(final long value) {
            this.value = value;
        }

        public boolean equals(final Object o) {
            if (!(o instanceof TestClass)) {
                return false;
            }
            return value == ((TestClass) o).value;
        }

        public int hashCode() {
            return Long.hashCode(value);
        }
    }
}
