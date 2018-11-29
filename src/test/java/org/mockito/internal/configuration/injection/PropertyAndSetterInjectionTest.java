package org.mockito.internal.configuration.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectUnsafe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.FINAL;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.NONE;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.STATIC;
import static org.mockito.InjectUnsafe.UnsafeFieldModifier.STATIC_FINAL;

public class PropertyAndSetterInjectionTest {

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static class InjectUnsafeTesting implements InjectUnsafe {
        private final UnsafeFieldModifier[] allow;

        private InjectUnsafeTesting(UnsafeFieldModifier... allow) {
            this.allow = allow;
        }

        @Override
        public UnsafeFieldModifier[] allow() {
            return allow;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return InjectUnsafe.class;
        }
    }

    @SuppressWarnings({ "FieldMayBeFinal", "FieldMayBeStatic" })
    public static class Foo {
        private int aInstance = 0;
        private final int aFinal = 0;
        private static int aStatic = 0;
        private static final int A_STATIC_FINAL = 0;
    }

    @SuppressWarnings({ "FieldMayBeFinal", "FieldMayBeStatic" })
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
     *
     * JaCoCo test coverage is implemented by instrumenting <i>any</i> class with a new field named "$jacocoData".
     * This field contains JaCoCo internal data structures.
     * It is only instrumented on gradle test runs and thus not present in production classes.
     * Thus we can simply ignore/remove this field in our tests.
     */
    private static void excludeJacocoTestField(List<Field> fields) {
        for (Field field : fields) {
            if ("$jacocoData".equals(field.getName())) {
                fields.remove(field);
                break;
            }
        }
    }

    @Test
    public void shouldBuildFieldList_ForField() {
        List<Field> fields = new PropertyAndSetterInjection()
            .orderedInstanceFieldsFrom(Foo.class, new InjectUnsafeTesting(NONE));
        excludeJacocoTestField(fields);

        assertThat(fields).containsExactly(FOO_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForField_WithSubclass() {
        List<Field> fields = new PropertyAndSetterInjection()
            .orderedInstanceFieldsFrom(Bar.class, new InjectUnsafeTesting(NONE));
        excludeJacocoTestField(fields);

        assertThat(fields).containsExactly(BAR_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForFinalField() {
        List<Field> fields = new PropertyAndSetterInjection()
            .orderedInstanceFieldsFrom(Foo.class, new InjectUnsafeTesting(FINAL));
        excludeJacocoTestField(fields);

        assertThat(fields).containsExactly(FOO_FINAL_FIELD, FOO_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForFinalField_WithSubclass() {
        List<Field> fields = new PropertyAndSetterInjection()
            .orderedInstanceFieldsFrom(Bar.class, new InjectUnsafeTesting(FINAL));
        excludeJacocoTestField(fields);

        assertThat(fields).containsExactly(BAR_FINAL_FIELD, BAR_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForStaticField() {
        List<Field> fields = new PropertyAndSetterInjection()
            .orderedInstanceFieldsFrom(Foo.class, new InjectUnsafeTesting(STATIC));
        excludeJacocoTestField(fields);

        assertThat(fields).containsExactly(FOO_INSTANCE_FIELD, FOO_STATIC_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForStaticField_WithSubclass() {
        List<Field> fields = new PropertyAndSetterInjection()
            .orderedInstanceFieldsFrom(Bar.class, new InjectUnsafeTesting(STATIC));
        excludeJacocoTestField(fields);

        assertThat(fields).containsExactly(BAR_INSTANCE_FIELD, BAR_STATIC_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForStaticFinalField() {
        List<Field> fields = new PropertyAndSetterInjection()
            .orderedInstanceFieldsFrom(Foo.class, new InjectUnsafeTesting(STATIC_FINAL));
        excludeJacocoTestField(fields);

        assertThat(fields).containsExactly(FOO_STATIC_FINAL_FIELD, FOO_INSTANCE_FIELD);
    }

    @Test
    public void shouldBuildFieldList_ForStaticFinalField_WithSubclass() {
        List<Field> fields = new PropertyAndSetterInjection()
            .orderedInstanceFieldsFrom(Bar.class, new InjectUnsafeTesting(STATIC_FINAL));
        excludeJacocoTestField(fields);

        assertThat(fields).containsExactly(BAR_STATIC_FINAL_FIELD, BAR_INSTANCE_FIELD);
    }

}
