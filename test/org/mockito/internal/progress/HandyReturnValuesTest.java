/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class HandyReturnValuesTest {

    private HandyReturnValues h = new HandyReturnValues();

    @Test
    public void should_not_return_null_for_primitives_wrappers() throws Exception {
        assertNotNull(h.returnFor(Boolean.class));
        assertNotNull(h.returnFor(Character.class));
        assertNotNull(h.returnFor(Byte.class));
        assertNotNull(h.returnFor(Short.class));
        assertNotNull(h.returnFor(Integer.class));
        assertNotNull(h.returnFor(Long.class));
        assertNotNull(h.returnFor(Float.class));
        assertNotNull(h.returnFor(Double.class));
    }

    @Test
    public void should_not_return_null_for_primitives() throws Exception {
        assertNotNull(h.returnFor(boolean.class));
        assertNotNull(h.returnFor(char.class));
        assertNotNull(h.returnFor(byte.class));
        assertNotNull(h.returnFor(short.class));
        assertNotNull(h.returnFor(int.class));
        assertNotNull(h.returnFor(long.class));
        assertNotNull(h.returnFor(float.class));
        assertNotNull(h.returnFor(double.class));
    }

    @Test public void should_default_values_for_primitive() {
        assertThat(h.returnFor(boolean.class)).isFalse();
        assertThat(h.returnFor(char.class)).isEqualTo('\u0000');
        assertThat(h.returnFor(byte.class)).isEqualTo((byte) 0);
        assertThat(h.returnFor(short.class)).isEqualTo((short) 0);
        assertThat(h.returnFor(int.class)).isEqualTo(0);
        assertThat(h.returnFor(long.class)).isEqualTo(0L);
        assertThat(h.returnFor(float.class)).isEqualTo(0.0F);
        assertThat(h.returnFor(double.class)).isEqualTo(0.0D);
    }

    @Test
    public void should_return_null_for_everything_else() throws Exception {
        assertNull(h.returnFor(Object.class));
        assertNull(h.returnFor(String.class));
        assertNull(h.returnFor(null));
    }

    @Test
    public void should_return_handy_value_for_instances() throws Exception {
        assertNull(h.returnFor(new Object()));
        assertNull(h.returnFor((Object) null));
        
        assertNotNull(h.returnFor(10.0));
        assertNotNull(h.returnFor(Boolean.FALSE));
    }
}
