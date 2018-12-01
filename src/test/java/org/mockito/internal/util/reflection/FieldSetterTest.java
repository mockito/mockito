/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * For the FieldSetterTest, separate Object-Under-Test-Class for every test method should be created.
 * This is because the Oracle-JDK implementation of Field.set() uses a cached accessor
 * (e.g. sun.reflect.UnsafeStaticFieldAccessorImpl).
 * Depending on test method ordering, the check for IllegalAccessExcpetion in Field.set() might occur in a state
 * when our FieldSetter has made the field accessible and thus setting a value is legal.
 * Subsequent calls of Field.set() on the same field use the cached accessor although the field modifiers probably
 * has been reset back to a state where IllegalAccessException should have occured.
 */
@SuppressWarnings({ "InstantiationOfUtilityClass", "PublicField", "FieldMayBeStatic",
    "UtilityClassWithoutPrivateConstructor", "NonFinalUtilityClass" })
public class FieldSetterTest {

    public static class InstanceField {
        public Integer field = null;
    }

    public static class FinalField {
        public final Integer finalField = null;
    }

    public static class StaticField {
        public static Integer staticField = null;
    }

    public static class StaticFinalField {
        @SuppressWarnings("FieldNamingConvention")
        public static final Integer staticFinalField = null;
    }

    // used for testing IllegalArgumentException
    public static class IllegalArgumentExceptionField {
        public Integer illegalArgumentExceptionField = null;
    }

    public static class SetAnyStaticFinalField {
        @SuppressWarnings("FieldNamingConvention")
        public static final Integer otherStaticFinalField = null;
    }

    private Field fieldRef = null;
    private Field finalFieldRef = null;
    private Field staticFieldRef = null;
    private Field staticFinalFieldRef = null;
    @SuppressWarnings("FieldAccessedSynchronizedAndUnsynchronized")
    private Field illegalArgumentExceptionFieldRef = null;
    @SuppressWarnings("FieldAccessedSynchronizedAndUnsynchronized")
    private Field otherStaticFinalFieldRef = null;

    @Before
    public void before() throws Exception {
        fieldRef = InstanceField.class.getDeclaredField("field");
        finalFieldRef = FinalField.class.getDeclaredField("finalField");
        staticFieldRef = StaticField.class.getDeclaredField("staticField");
        staticFinalFieldRef = StaticFinalField.class.getDeclaredField("staticFinalField");
        illegalArgumentExceptionFieldRef = IllegalArgumentExceptionField.class
            .getDeclaredField("illegalArgumentExceptionField");
        otherStaticFinalFieldRef = SetAnyStaticFinalField.class.getDeclaredField("otherStaticFinalField");
    }

    @Test
    public void setField_instanceField() {
        InstanceField foo = new InstanceField();

        FieldSetter.setField(foo, fieldRef, 42);

        assertThat(foo.field).isEqualTo(42);
    }

    @Test
    public void setField_finalField() {
        FinalField foo = new FinalField();

        FieldSetter.setField(foo, finalFieldRef, 42);

        assertThat(foo.finalField).isEqualTo(42);
    }

    @Test
    public void setField_staticField() {
        StaticField foo = new StaticField();
        // sanity check since we're dealing with static
        assertThat(StaticField.staticField).isNull();

        FieldSetter.setField(foo, staticFieldRef, 42);

        assertThat(StaticField.staticField).isEqualTo(42);
    }

    @Test
    public void setField_staticFinalFieldShouldThrowIllegalAccessException() {
        StaticFinalField foo = new StaticFinalField();
        // sanity check since we're dealing with static
        assertThat(StaticFinalField.staticFinalField).isNull();

        try {
            FieldSetter.setField(foo, staticFinalFieldRef, 42);
            fail();
        } catch (RuntimeException rte) {
            assertThat(rte.getMessage()).startsWith("Access not authorized on field");
        }
    }

    @Test
    public synchronized void setField_shouldThrowIllegalArgumentException() {
        IllegalArgumentExceptionField foo = new IllegalArgumentExceptionField();
        try {
            FieldSetter.setField(foo, illegalArgumentExceptionFieldRef, new Object());
            fail();
        } catch (RuntimeException rte) {
            assertThat(rte.getMessage()).startsWith("Wrong argument on field");
        }
    }

    @Test
    public synchronized void setAnyField_staticFinalFieldShouldWork() {
        SetAnyStaticFinalField foo = new SetAnyStaticFinalField();
        // sanity check since we're dealing with static
        assertThat(SetAnyStaticFinalField.otherStaticFinalField).isNull();

        FieldSetter.setAnyField(foo, otherStaticFinalFieldRef, 42);

        assertThat(SetAnyStaticFinalField.otherStaticFinalField).isEqualTo(42);
    }
}
