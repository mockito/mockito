/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.assertj.core.api.AbstractAssert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.IntStream;

import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.STATIC;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.internal.util.reflection.StaticFinalOverriderTest.FieldAssertions.assertThatField;

public class StaticFinalOverriderTest {

    private static class StaticFinalFixture {
        private static final Integer STATIC_FINAL = null;
    }

    private static class StaticFixture {
        @SuppressWarnings("FieldMayBeFinal")
        private static Integer STATIC = null;
    }

    private static class FinalFixture {
        private final Integer FINAL = null;
    }

    private static class NormalFixture {
        @SuppressWarnings("FieldMayBeFinal")
        private Integer NORMAL = null;
    }

    @Test
    public void static_final_field_is_made_writable() throws NoSuchFieldException {
        Field field = StaticFinalFixture.class.getDeclaredField("STATIC_FINAL");
        int originalModifiers = field.getModifiers();
        // sanity check
        assertThatField(field).includesModifiers(STATIC, FINAL);

        StaticFinalOverrider overrider = StaticFinalOverrider.forField(field);

        overrider.enableWrite();

        assertThatField(field).includesModifiers(STATIC).doesNotIncludeModifiers(FINAL);

        overrider.restore();

        assertThatField(field).hasModifiers(originalModifiers);
    }

    @Test
    public void static_field_is_made_writable() throws NoSuchFieldException {
        Field field = StaticFixture.class.getDeclaredField("STATIC");
        int originalModifiers = field.getModifiers();
        // sanity check
        assertThatField(field).includesModifiers(STATIC).doesNotIncludeModifiers(FINAL);

        StaticFinalOverrider overrider = StaticFinalOverrider.forField(field);

        overrider.enableWrite();

        assertThatField(field).includesModifiers(STATIC).doesNotIncludeModifiers(FINAL);

        overrider.restore();

        assertThatField(field).hasModifiers(originalModifiers);
    }

    @Test
    public void final_field_is_made_writable() throws NoSuchFieldException {
        Field field = FinalFixture.class.getDeclaredField("FINAL");
        int originalModifiers = field.getModifiers();
        // sanity check
        assertThatField(field).doesNotIncludeModifiers(STATIC).includesModifiers(FINAL);

        StaticFinalOverrider overrider = StaticFinalOverrider.forField(field);

        overrider.enableWrite();

        assertThatField(field).doesNotIncludeModifiers(STATIC, FINAL);

        overrider.restore();

        assertThatField(field).hasModifiers(originalModifiers);
    }

    @Test
    public void normal_field_needs_no_modification() throws NoSuchFieldException {
        Field field = NormalFixture.class.getDeclaredField("NORMAL");
        int originalModifiers = field.getModifiers();
        // sanity check
        assertThatField(field).doesNotIncludeModifiers(STATIC, FINAL);

        StaticFinalOverrider overrider = StaticFinalOverrider.forField(field);

        overrider.enableWrite();

        assertThatField(field).doesNotIncludeModifiers(STATIC, FINAL);

        overrider.restore();

        assertThatField(field).hasModifiers(originalModifiers);
    }

    @Test
    public void restoring_modifiers_without_enabling_first_throws_exception()
            throws NoSuchFieldException {
        Field field = StaticFinalFixture.class.getDeclaredField("STATIC_FINAL");
        StaticFinalOverrider overrider = StaticFinalOverrider.forField(field);

        assertThatThrownBy(() -> overrider.restore())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(field.toString());
    }

    public static class FieldAssertions extends AbstractAssert<FieldAssertions, Field> {

        protected FieldAssertions(Field field) {
            super(field, FieldAssertions.class);
        }

        public static FieldAssertions assertThatField(Field field) {
            return new FieldAssertions(field);
        }

        /**
         * Field modifiers must match exactly.
         */
        public FieldAssertions hasModifiers(int... expected) {
            int allExpected = IntStream.of(expected).sum();

            @SuppressWarnings("MagicConstant")
            boolean modifiersMatch = actual.getModifiers() == allExpected;

            if (!modifiersMatch) {
                String actualModifiers = Modifier.toString(actual.getModifiers());
                String expectedModifiers = Modifier.toString(allExpected);

                failWithActualExpectedAndMessage(
                        actual.getModifiers(),
                        allExpected,
                        "Expecting field modifiers to be:\n  '%s'\nbut was:\n  '%s'",
                        expectedModifiers,
                        actualModifiers);
            }

            return this;
        }

        /**
         * Field modifiers must include expected modifiers (but may also contain others).
         */
        public FieldAssertions includesModifiers(int... expected) {
            int allExpected = IntStream.of(expected).sum();

            int intersection = actual.getModifiers() & allExpected;
            boolean modifiersMatch = intersection == allExpected;

            if (!modifiersMatch) {
                String actualModifiers = Modifier.toString(actual.getModifiers());
                String expectedModifiers = Modifier.toString(allExpected);
                int missing = allExpected - intersection;
                String missingModifiers = Modifier.toString(missing);

                failWithActualExpectedAndMessage(
                        actual.getModifiers(),
                        allExpected,
                        "Expecting field modifiers to include:\n  '%s'\nbut was:\n  '%s'\nMissing:\n  '%s'",
                        expectedModifiers,
                        actualModifiers,
                        missingModifiers);
            }

            return this;
        }

        /**
         * Field modifiers must <strong>not</strong> include the given modifies.
         */
        public FieldAssertions doesNotIncludeModifiers(int... unexpected) {
            int allExcluded = IntStream.of(unexpected).sum();

            int intersection = actual.getModifiers() & allExcluded;
            boolean foundExcluded = intersection != 0;

            if (foundExcluded) {
                String actualModifiers = Modifier.toString(actual.getModifiers());
                String notExpectedModifiers = Modifier.toString(allExcluded);
                String surplus = Modifier.toString(intersection);

                failWithActualExpectedAndMessage(
                        actual.getModifiers(),
                        allExcluded,
                        "Expecting field modifiers to *not* include:\n  '%s'\nbut were:\n  '%s'\nSurplus:\n  '%s'",
                        notExpectedModifiers,
                        actualModifiers,
                        surplus);
            }

            return this;
        }
    }
}
