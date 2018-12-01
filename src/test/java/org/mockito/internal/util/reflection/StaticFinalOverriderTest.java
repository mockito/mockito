/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

import org.junit.Test;

import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.STATIC;
import static org.assertj.core.api.Assertions.assertThat;

public class StaticFinalOverriderTest {

    private static class Foo {
        private static final Integer BAR = null;
    }

    private static final Field FIELD;

    static {
        try {
            FIELD = Foo.class.getDeclaredField("BAR");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("field not found", e);
        }
    }

    @Test
    public void testEverything() {
        StaticFinalOverrider overrider = new StaticFinalOverrider();

        // assert the state is the same before and after
        assertModifiersBeforeAndAfter(FIELD);

        overrider.enableWrite(FIELD);

        assertThat(hasModifier(FIELD, STATIC)).isTrue();
        assertThat(hasModifier(FIELD, FINAL)).isFalse();
        assertThat(FIELD.isAccessible()).isFalse();

        overrider.safelyDisableWrite(FIELD);

        assertModifiersBeforeAndAfter(FIELD);
    }

    private static void assertModifiersBeforeAndAfter(Field field) {
        assertThat(hasModifier(field, STATIC)).isTrue();
        assertThat(hasModifier(field, FINAL)).isTrue();
        assertThat(field.isAccessible()).isFalse();
    }

    private static boolean hasModifier(Field field, int modifier) {
        return (field.getModifiers() & modifier) != 0;
    }

}
