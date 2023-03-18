/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class SpyWithConstructorTest {

    private SomethingAbstract somethingAbstract;

    @Before
    public void setUp() {
        somethingAbstract = mock(SomethingAbstract.class, withSettings()
            .useConstructor("foo")
            .defaultAnswer(CALLS_REAL_METHODS));
    }

    @Test
    public void shouldUseConstructor() {
        assertEquals("foo", somethingAbstract.getValue());
    }

    static abstract class SomethingAbstract {

        private final String value;

        SomethingAbstract(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
