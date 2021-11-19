/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static org.junit.Assert.*;

import org.junit.Test;

public class IdentitySetTest {

    IdentitySet set = new IdentitySet();

    @Test
    public void shouldWork() throws Exception {
        // when
        Object o = new Object();
        set.add(o);

        // then
        assertTrue(set.contains(o));
        assertFalse(set.contains(new Object()));
    }

    @SuppressWarnings("EqualsHashCode")
    class Fake {
        @Override
        public boolean equals(Object obj) {
            return true;
        }
    }

    @Test
    public void shouldWorkEvenIfEqualsTheSame() throws Exception {
        // given
        assertEquals(new Fake(), new Fake());
        Fake fake = new Fake();

        // when
        set.add(fake);

        // then
        assertTrue(set.contains(fake));
        assertFalse(set.contains(new Fake()));
    }
}
