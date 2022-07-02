/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.InjectUnsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.*;

public class PropertyAndSetterInjectionTest {

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static class InjectUnsafeTesting implements InjectUnsafe {
        private final UnsafeFieldModifier allow;

        private InjectUnsafeTesting(UnsafeFieldModifier allow) {
            this.allow = allow;
        }

        @Override
        public UnsafeFieldModifier value() {
            return allow;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return InjectUnsafe.class;
        }
    }

    @SuppressWarnings({"FieldMayBeFinal", "FieldMayBeStatic"})
    public static class Foo {
        private int aInstance = 0;
        private final int aFinal = 0;
        private static int aStatic = 0;
        private static final int A_STATIC_FINAL = 0;
    }

    @SuppressWarnings({"FieldMayBeFinal", "FieldMayBeStatic"})
    public static class Bar extends Foo {
        private int zInstance = 0;
        private final int zFinal = 0;
        private static int zStatic = 0;
        private static final int Z_STATIC_FINAL = 0;
    }

    private static final Field FOO_INSTANCE_FIELD;
    private static final Field FOO_FINAL_FIELD;
    private static final Field FOO_STATIC_FIELD;
    private static final Field FOO_STATIC_FINAL_FIELD;
    private static final Field BAR_INSTANCE_FIELD;
    private static final Field BAR_FINAL_FIELD;
    private static final Field BAR_STATIC_FIELD;
    private static final Field BAR_STATIC_FINAL_FIELD;

    static {
        try {
            FOO_INSTANCE_FIELD = Foo.class.getDeclaredField("aInstance");
            FOO_FINAL_FIELD = Foo.class.getDeclaredField("aFinal");
            FOO_STATIC_FIELD = Foo.class.getDeclaredField("aStatic");
            FOO_STATIC_FINAL_FIELD = Foo.class.getDeclaredField("A_STATIC_FINAL");
            BAR_INSTANCE_FIELD = Bar.class.getDeclaredField("zInstance");
            BAR_FINAL_FIELD = Bar.class.getDeclaredField("zFinal");
            BAR_STATIC_FIELD = Bar.class.getDeclaredField("zStatic");
            BAR_STATIC_FINAL_FIELD = Bar.class.getDeclaredField("Z_STATIC_FINAL");
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Test setup failure: field does not exist: " + e);
        }
    }

    /**
     * Exclude test-internal fields (JaCoCo test coverage).
     * <p>
     * JaCoCo test coverage is implemented by instrumenting <i>any</i> class with a new field named "$jacocoData".
     * This field contains JaCoCo internal data structures.
     * It is only instrumented on gradle test runs and thus not present in production classes.
     * Thus we can simply ignore/remove this field in our tests.
     */
    private static void excludeCoverageEngineFields(List<Field> fields) {
        Predicate<Field> isJaCoCoField = f -> f.getName().startsWith("$jacoco");
        Predicate<Field> isIntelliJField = f -> f.getName().equals("__$lineHits$__");

        fields.removeIf(isJaCoCoField.or(isIntelliJField));
    }

    @Test
    public void shouldBuildFieldList_ForField() {
        List<Field> fields =
                new PropertyAndSetterInjection()
                        .orderedInstanceFieldsFrom(Foo.class, new InjectUnsafeTesting(NONE));
        excludeCoverageEngineFields(fields);

        assertThat(fields).containsExactly(FOO_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForField_WithSubclass() {
        List<Field> fields =
                new PropertyAndSetterInjection()
                        .orderedInstanceFieldsFrom(Bar.class, new InjectUnsafeTesting(NONE));
        excludeCoverageEngineFields(fields);

        assertThat(fields).containsExactly(BAR_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForFinalField() {
        List<Field> fields =
                new PropertyAndSetterInjection()
                        .orderedInstanceFieldsFrom(Foo.class, new InjectUnsafeTesting(FINAL));
        excludeCoverageEngineFields(fields);

        assertThat(fields).containsExactly(FOO_FINAL_FIELD, FOO_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForFinalField_WithSubclass() {
        List<Field> fields =
                new PropertyAndSetterInjection()
                        .orderedInstanceFieldsFrom(Bar.class, new InjectUnsafeTesting(FINAL));
        excludeCoverageEngineFields(fields);

        assertThat(fields).containsExactly(BAR_FINAL_FIELD, BAR_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForStaticField() {
        List<Field> fields =
                new PropertyAndSetterInjection()
                        .orderedInstanceFieldsFrom(Foo.class, new InjectUnsafeTesting(STATIC));
        excludeCoverageEngineFields(fields);

        assertThat(fields).containsExactly(FOO_INSTANCE_FIELD, FOO_STATIC_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForStaticField_WithSubclass() {
        List<Field> fields =
                new PropertyAndSetterInjection()
                        .orderedInstanceFieldsFrom(Bar.class, new InjectUnsafeTesting(STATIC));
        excludeCoverageEngineFields(fields);

        assertThat(fields).containsExactly(BAR_INSTANCE_FIELD, BAR_STATIC_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForStaticFinalField() {
        List<Field> fields =
                new PropertyAndSetterInjection()
                        .orderedInstanceFieldsFrom(
                                Foo.class, new InjectUnsafeTesting(STATIC_FINAL));
        excludeCoverageEngineFields(fields);

        assertThat(fields).containsExactly(FOO_STATIC_FINAL_FIELD, FOO_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForStaticFinalField_WithSubclass() {
        List<Field> fields =
                new PropertyAndSetterInjection()
                        .orderedInstanceFieldsFrom(
                                Bar.class, new InjectUnsafeTesting(STATIC_FINAL));
        excludeCoverageEngineFields(fields);

        assertThat(fields).containsExactly(BAR_STATIC_FINAL_FIELD, BAR_INSTANCE_FIELD);
    }
}
