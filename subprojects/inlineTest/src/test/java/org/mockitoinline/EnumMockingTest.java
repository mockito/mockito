/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class EnumMockingTest {

    @Test
    public void testMockEnum() {
        Animal a = Mockito.mock(Animal.class);
        assertThat(a, not(equalTo(Animal.CAT)));
        assertThat(a.sound(), nullValue(String.class));
        assertThat(a.name(), nullValue(String.class));
    }

    enum Animal {
        CAT {
            @Override
            public String sound() {
                return "meow";
            }
        };

        public abstract String sound();
    }
}
