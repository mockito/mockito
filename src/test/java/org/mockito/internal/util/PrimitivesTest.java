/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class PrimitivesTest {
    @Test
    public void should_not_return_null_for_primitives_wrappers() throws Exception {
        assertNotNull(Primitives.defaultValue(Boolean.class));
        assertNotNull(Primitives.defaultValue(Character.class));
        assertNotNull(Primitives.defaultValue(Byte.class));
        assertNotNull(Primitives.defaultValue(Short.class));
        assertNotNull(Primitives.defaultValue(Integer.class));
        assertNotNull(Primitives.defaultValue(Long.class));
        assertNotNull(Primitives.defaultValue(Float.class));
        assertNotNull(Primitives.defaultValue(Double.class));
    }

    @Test
    public void should_not_return_null_for_primitives() throws Exception {
        assertNotNull(Primitives.defaultValue(boolean.class));
        assertNotNull(Primitives.defaultValue(char.class));
        assertNotNull(Primitives.defaultValue(byte.class));
        assertNotNull(Primitives.defaultValue(short.class));
        assertNotNull(Primitives.defaultValue(int.class));
        assertNotNull(Primitives.defaultValue(long.class));
        assertNotNull(Primitives.defaultValue(float.class));
        assertNotNull(Primitives.defaultValue(double.class));
    }

    @Test 
    public void should_default_values_for_primitive() {
        assertThat(Primitives.defaultValue(boolean.class)).isFalse();
        assertThat(Primitives.defaultValue(char.class)).isEqualTo('\u0000');
        assertThat(Primitives.defaultValue(byte.class)).isEqualTo((byte) 0);
        assertThat(Primitives.defaultValue(short.class)).isEqualTo((short) 0);
        assertThat(Primitives.defaultValue(int.class)).isEqualTo(0);
        assertThat(Primitives.defaultValue(long.class)).isEqualTo(0L);
        assertThat(Primitives.defaultValue(float.class)).isEqualTo(0.0F);
        assertThat(Primitives.defaultValue(double.class)).isEqualTo(0.0D);
    }
    
    @Test 
    public void should_default_values_for_wrapper() {
        assertThat(Primitives.defaultValue(Boolean.class)).isFalse();
        assertThat(Primitives.defaultValue(Character.class)).isEqualTo('\u0000');
        assertThat(Primitives.defaultValue(Byte.class)).isEqualTo((byte) 0);
        assertThat(Primitives.defaultValue(Short.class)).isEqualTo((short) 0);
        assertThat(Primitives.defaultValue(Integer.class)).isEqualTo(0);
        assertThat(Primitives.defaultValue(Long.class)).isEqualTo(0L);
        assertThat(Primitives.defaultValue(Float.class)).isEqualTo(0.0F);
        assertThat(Primitives.defaultValue(Double.class)).isEqualTo(0.0D);
    }

    @Test
    public void should_return_null_for_everything_else() throws Exception {
        assertNull(Primitives.defaultValue(Object.class));
        assertNull(Primitives.defaultValue(String.class));
        assertNull(Primitives.defaultValue(null));
    }

    @Test
    public void should_check_that_value_type_is_assignable_to_wrapper_reference() {
        assertThat(Primitives.isAssignableFromWrapper(int.class, Integer.class));
        assertThat(Primitives.isAssignableFromWrapper(Integer.class, Integer.class));
        assertThat(Primitives.isAssignableFromWrapper(long.class, Long.class));
        assertThat(Primitives.isAssignableFromWrapper(Long.class, Long.class));
        assertThat(Primitives.isAssignableFromWrapper(double.class, Double.class));
        assertThat(Primitives.isAssignableFromWrapper(Double.class, Double.class));
        assertThat(Primitives.isAssignableFromWrapper(float.class, Float.class));
        assertThat(Primitives.isAssignableFromWrapper(Float.class, Float.class));
        assertThat(Primitives.isAssignableFromWrapper(char.class, Character.class));
        assertThat(Primitives.isAssignableFromWrapper(Character.class, Character.class));
        assertThat(Primitives.isAssignableFromWrapper(short.class, Short.class));
        assertThat(Primitives.isAssignableFromWrapper(Short.class, Short.class));
        assertThat(Primitives.isAssignableFromWrapper(byte.class, Byte.class));
        assertThat(Primitives.isAssignableFromWrapper(Byte.class, Byte.class));
        assertThat(Primitives.isAssignableFromWrapper(boolean.class, Boolean.class));
        assertThat(Primitives.isAssignableFromWrapper(Boolean.class, Boolean.class));
    }
}