/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import static java.lang.Thread.currentThread;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockitoutil.ClassLoaders.currentClassLoader;
import static org.mockitoutil.ClassLoaders.excludingClassLoader;
import static org.mockitoutil.ClassLoaders.isolatedClassLoader;
import static org.mockitoutil.ClassLoaders.jdkClassLoader;

import org.junit.Test;
import org.mockito.Mockito;

public class ClassLoadersTest {

    public static final String CLASS_NAME_DEPENDING_ON_INTERFACE =
            "org.mockitoutil.ClassLoadersTest$ClassUsingInterface1";
    public static final String INTERFACE_NAME = "org.mockitoutil.ClassLoadersTest$Interface1";

    @Test
    public void isolated_class_loader_cannot_load_classes_when_no_given_prefix() {
        // given
        ClassLoader cl = isolatedClassLoader().build();

        // when / then
        assertThatThrownBy(
                        () -> {
                            cl.loadClass("org.mockito.Mockito");
                        })
                .isInstanceOf(ClassNotFoundException.class)
                .hasMessage("Can only load classes with prefixes : [], but not : []");
    }

    @Test
    public void isolated_class_loader_cannot_load_classes_if_no_code_source_path() {
        // given
        ClassLoader cl =
                isolatedClassLoader().withPrivateCopyOf(CLASS_NAME_DEPENDING_ON_INTERFACE).build();

        // when
        try {
            cl.loadClass(CLASS_NAME_DEPENDING_ON_INTERFACE);
            fail();
        } catch (ClassNotFoundException e) {
            // then
            assertThat(e).hasMessageContaining(CLASS_NAME_DEPENDING_ON_INTERFACE);
        }
    }

    @Test
    public void
            isolated_class_loader_cannot_load_classes_if_dependent_classes_do_not_match_the_prefixes()
                    throws Exception {
        // given
        ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withPrivateCopyOf(CLASS_NAME_DEPENDING_ON_INTERFACE)
                        .build();

        // when
        try {
            cl.loadClass(CLASS_NAME_DEPENDING_ON_INTERFACE);
            fail();
        } catch (NoClassDefFoundError e) {
            // then
            assertThat(e).hasMessageContaining("org/mockitoutil/ClassLoadersTest$Interface1");
        }
    }

    @Test
    public void
            isolated_class_loader_can_load_classes_when_dependent_classes_are_matching_the_prefixes()
                    throws Exception {
        // given
        ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withPrivateCopyOf(CLASS_NAME_DEPENDING_ON_INTERFACE)
                        .withPrivateCopyOf(INTERFACE_NAME)
                        .build();

        // when
        Class<?> aClass = cl.loadClass(CLASS_NAME_DEPENDING_ON_INTERFACE);

        // then
        assertThat(aClass).isNotNull();
        assertThat(aClass.getClassLoader()).isEqualTo(cl);
        assertThat(aClass.getInterfaces()[0].getClassLoader()).isEqualTo(cl);
    }

    @Test
    public void isolated_class_loader_can_load_classes_isolated_classes_in_isolation()
            throws Exception {
        // given
        ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withPrivateCopyOf(ClassLoadersTest.class.getPackage().getName())
                        .build();

        // when
        Class<?> aClass = cl.loadClass(AClass.class.getName());

        // then
        assertThat(aClass).isNotNull();
        assertThat(aClass).isNotSameAs(AClass.class);
        assertThat(aClass.getClassLoader()).isEqualTo(cl);
    }

    @Test
    public void isolated_class_loader_cannot_load_classes_if_prefix_excluded() {
        // given
        ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withPrivateCopyOf(ClassLoadersTest.class.getPackage().getName())
                        .without(AClass.class.getName())
                        .build();

        // when
        try {
            cl.loadClass(AClass.class.getName());
            fail();
        } catch (ClassNotFoundException e) {
            // then
            assertThat(e)
                    .hasMessageContaining("org.mockitoutil")
                    .hasMessageContaining(AClass.class.getName());
        }
    }

    @Test
    public void isolated_class_loader_has_no_parent() {
        ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withPrivateCopyOf(CLASS_NAME_DEPENDING_ON_INTERFACE)
                        .withPrivateCopyOf(INTERFACE_NAME)
                        .build();

        assertThat(cl.getParent()).isNull();
    }

    @Test
    public void excluding_class_loader_cannot_load_classes_when_no_correct_source_url_set() {
        // given
        ClassLoader cl = excludingClassLoader().withCodeSourceUrlOf(this.getClass()).build();

        // when / then
        assertThatThrownBy(
                        () -> {
                            cl.loadClass("org.mockito.Mockito");
                        })
                .isInstanceOf(ClassNotFoundException.class)
                .hasMessage("org.mockito.Mockito");
    }

    @Test
    public void excluding_class_loader_can_load_classes_when_correct_source_url_set()
            throws Exception {
        // given
        ClassLoader cl = excludingClassLoader().withCodeSourceUrlOf(Mockito.class).build();

        // when
        cl.loadClass("org.mockito.Mockito");

        // then class successfully loaded
    }

    @Test
    public void excluding_class_loader_cannot_load_class_when_excluded_prefix_match_class_to_load()
            throws Exception {
        // given
        ClassLoader cl =
                excludingClassLoader()
                        .withCodeSourceUrlOf(Mockito.class)
                        .without("org.mockito.BDDMockito")
                        .build();

        cl.loadClass("org.mockito.Mockito");

        // when
        try {
            cl.loadClass("org.mockito.BDDMockito");
            fail("should have raise a ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            assertThat(e.getMessage()).contains("org.mockito.BDDMockito");
        }

        // then class successfully loaded
    }

    @Test
    public void can_not_load_a_class_not_previously_registered_in_builder() {
        // given
        ClassLoader cl =
                ClassLoaders.inMemoryClassLoader()
                        .withClassDefinition(
                                "yop.Dude", SimpleClassGenerator.makeMarkerInterface("yop.Dude"))
                        .build();

        // when
        try {
            cl.loadClass("not.Defined");
            fail();
        } catch (ClassNotFoundException e) {
            // then
            assertThat(e.getMessage()).contains("not.Defined");
        }
    }

    @Test
    public void can_load_a_class_in_memory_from_bytes() throws Exception {
        // given
        ClassLoader cl =
                ClassLoaders.inMemoryClassLoader()
                        .withClassDefinition(
                                "yop.Dude", SimpleClassGenerator.makeMarkerInterface("yop.Dude"))
                        .build();

        // when
        Class<?> aClass = cl.loadClass("yop.Dude");

        // then
        assertThat(aClass).isNotNull();
        assertThat(aClass.getClassLoader()).isEqualTo(cl);
        assertThat(aClass.getName()).isEqualTo("yop.Dude");
    }

    @Test
    public void cannot_load_a_class_file_not_in_parent() throws Exception {
        // given
        ClassLoader cl = ClassLoaders.inMemoryClassLoader().withParent(jdkClassLoader()).build();

        cl.loadClass("java.lang.String");

        try {
            // when
            cl.loadClass("org.mockito.Mockito");
            fail("should have not found Mockito class");
        } catch (ClassNotFoundException e) {
            // then
            assertThat(e.getMessage()).contains("org.mockito.Mockito");
        }
    }

    @Test
    public void can_list_all_classes_reachable_in_a_classloader() throws Exception {
        ClassLoader classLoader =
                ClassLoaders.inMemoryClassLoader()
                        .withParent(jdkClassLoader())
                        .withClassDefinition("a.A", SimpleClassGenerator.makeMarkerInterface("a.A"))
                        .withClassDefinition(
                                "a.b.B", SimpleClassGenerator.makeMarkerInterface("a.b.B"))
                        .withClassDefinition("c.C", SimpleClassGenerator.makeMarkerInterface("c.C"))
                        //                .withCodeSourceUrlOf(ClassLoaders.class)
                        .build();

        assertThat(ClassLoaders.in(classLoader).listOwnedClasses())
                .containsOnly("a.A", "a.b.B", "c.C");
        assertThat(ClassLoaders.in(classLoader).omit("b", "c").listOwnedClasses())
                .containsOnly("a.A");
    }

    @Test
    public void return_bootstrap_classloader() {
        assertThat(jdkClassLoader()).isNotEqualTo(Mockito.class.getClassLoader());
        assertThat(jdkClassLoader()).isNotEqualTo(ClassLoaders.class.getClassLoader());
        assertThat(jdkClassLoader()).isEqualTo(Number.class.getClassLoader());
        assertThat(jdkClassLoader()).isEqualTo(null);
    }

    @Test
    public void return_current_classloader() {
        assertThat(currentClassLoader()).isEqualTo(this.getClass().getClassLoader());
    }

    @Test
    public void can_run_in_given_classloader() throws Exception {
        ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withPrivateCopyOf(CLASS_NAME_DEPENDING_ON_INTERFACE, INTERFACE_NAME)
                        .build();

        Class<?> loadedClass =
                ClassLoaders.using(cl)
                        .execute(
                                () -> {
                                    ClassLoader contextLoader =
                                            currentThread().getContextClassLoader();
                                    assertThat(contextLoader).isSameAs(cl);
                                    assertThat(contextLoader.loadClass(String.class.getName()))
                                            .isSameAs(String.class);
                                    return contextLoader.loadClass(
                                            CLASS_NAME_DEPENDING_ON_INTERFACE);
                                });

        assertThat(loadedClass.getClassLoader()).isSameAs(cl);
    }

    @Test
    public void cannot_load_class_from_task_when_dependency_is_excluded() {
        ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withPrivateCopyOf(CLASS_NAME_DEPENDING_ON_INTERFACE, INTERFACE_NAME)
                        .without(INTERFACE_NAME)
                        .build();

        var executor = ClassLoaders.using(cl);
        assertThatThrownBy(
                        () ->
                                executor.execute(
                                        () -> {
                                            ClassLoader contextLoader =
                                                    currentThread().getContextClassLoader();
                                            return contextLoader.loadClass(
                                                    CLASS_NAME_DEPENDING_ON_INTERFACE);
                                        }))
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(NoClassDefFoundError.class)
                .hasMessageContaining("ClassLoadersTest$Interface1");
    }

    @SuppressWarnings("unused")
    static class AClass {}

    @SuppressWarnings("unused")
    static class ClassUsingInterface1 implements Interface1 {}

    @SuppressWarnings("unused")
    interface Interface1 {}
}
