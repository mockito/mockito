/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldSetterTest {

    public static class Foo {
        private Integer field = null;
        private final Integer finalField = null;
        private static Integer staticField = null;
        private static final Integer staticFinalField = null;

        public Integer getField() {
            return field;
        }

        public Integer getFinalField() {
            return finalField;
        }

        public static Integer getStaticField() {
            return staticField;
        }

        public static Integer getStaticFinalField() {
            return staticFinalField;
        }
    }

    private static final Field fieldRef;
    private static final Field finalFieldRef;
    private static final Field staticFieldRef;
    private static final Field staticFinalFieldRef;

    static {
        try {
            fieldRef = Foo.class.getDeclaredField("field");
            finalFieldRef = Foo.class.getDeclaredField("finalField");
            staticFieldRef = Foo.class.getDeclaredField("staticField");
            staticFinalFieldRef = Foo.class.getDeclaredField("staticFinalField");
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Test setup error!", e);
        }
    }


    @Test
    public void setField_instanceField() {
        Foo foo = new Foo();

        FieldSetter.setField(foo, fieldRef, 42);

        assertThat(foo.getField()).isEqualTo(42);
    }

    @Test
    public void setField_finalField() {
        Foo foo = new Foo();

        FieldSetter.setField(foo, finalFieldRef, 42);

        assertThat(foo.getFinalField()).isEqualTo(42);
    }
    @Test
    public void setField_staticField() {
        Foo foo = new Foo();
        assertThat(Foo.getStaticField()).isNull(); // sanity check since we're dealing sith statics

        FieldSetter.setField(foo, staticFieldRef, 42);

        assertThat(Foo.getStaticField()).isEqualTo(42);
    }

    @Test
    public void setField_staticFinalField() {
        Foo foo = new Foo();
        assertThat(Foo.getStaticFinalField()).isNull(); // sanity check since we're dealing sith statics

        FieldSetter.setField(foo, staticFinalFieldRef, 42);

        assertThat(Foo.getStaticFinalField()).isEqualTo(42);
    }
}
