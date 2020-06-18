/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockitoutil.ClassLoaders.currentClassLoader;
import static org.mockitoutil.ClassLoaders.excludingClassLoader;
import static org.mockitoutil.ClassLoaders.isolatedClassLoader;
import static org.mockitoutil.ClassLoaders.jdkClassLoader;

import java.util.concurrent.atomic.AtomicBoolean;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class ClassLoadersTest {

    public static final String CLASS_NAME_DEPENDING_ON_INTERFACE =
            "org.mockitoutil.ClassLoadersTest$ClassUsingInterface1";
    public static final String INTERFACE_NAME = "org.mockitoutil.ClassLoadersTest$Interface1";

    @Test(expected = ClassNotFoundException.class)
    public void isolated_class_loader_cannot_load_classes_when_no_given_prefix() throws Exception {
        // given
        ClassLoader cl = isolatedClassLoader().build();

        // when
        cl.loadClass("org.mockito.Mockito");

        // then raises CNFE
    }

    @Test
    public void isolated_class_loader_cannot_load_classes_if_no_code_source_path()
            throws Exception {
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
    public void isolated_class_loader_cannot_load_classes_if_prefix_excluded() throws Exception {
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
    public void isolated_class_loader_has_no_parent() throws Exception {
        ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withPrivateCopyOf(CLASS_NAME_DEPENDING_ON_INTERFACE)
                        .withPrivateCopyOf(INTERFACE_NAME)
                        .build();

        assertThat(cl.getParent()).isNull();
    }

    @Test(expected = ClassNotFoundException.class)
    public void excluding_class_loader_cannot_load_classes_when_no_correct_source_url_set()
            throws Exception {
        // given
        ClassLoader cl = excludingClassLoader().withCodeSourceUrlOf(this.getClass()).build();

        // when
        cl.loadClass("org.mockito.Mockito");

        // then class CNFE
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
    public void can_not_load_a_class_not_previously_registered_in_builder() throws Exception {
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
    public void return_bootstrap_classloader() throws Exception {
        assertThat(jdkClassLoader()).isNotEqualTo(Mockito.class.getClassLoader());
        assertThat(jdkClassLoader()).isNotEqualTo(ClassLoaders.class.getClassLoader());
        assertThat(jdkClassLoader()).isEqualTo(Number.class.getClassLoader());
        assertThat(jdkClassLoader()).isEqualTo(null);
    }

    @Test
    public void return_current_classloader() throws Exception {
        assertThat(currentClassLoader()).isEqualTo(this.getClass().getClassLoader());
    }

    @Test
    public void can_run_in_given_classloader() throws Exception {
        // given
        final ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withCodeSourceUrlOf(Assertions.class)
                        .withPrivateCopyOf("org.assertj.core")
                        .withPrivateCopyOf(ClassLoadersTest.class.getPackage().getName())
                        .without(AClass.class.getName())
                        .build();

        final AtomicBoolean executed = new AtomicBoolean(false);

        // when
        ClassLoaders.using(cl)
                .execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                assertThat(this.getClass().getClassLoader())
                                        .describedAs("runnable is reloaded in given classloader")
                                        .isEqualTo(cl);
                                assertThat(Thread.currentThread().getContextClassLoader())
                                        .describedAs(
                                                "Thread context classloader is using given classloader")
                                        .isEqualTo(cl);

                                try {
                                    assertThat(
                                                    Thread.currentThread()
                                                            .getContextClassLoader()
                                                            .loadClass("java.lang.String"))
                                            .describedAs("can load JDK type")
                                            .isNotNull();
                                    assertThat(
                                                    Thread.currentThread()
                                                            .getContextClassLoader()
                                                            .loadClass(
                                                                    "org.mockitoutil.ClassLoadersTest$ClassUsingInterface1"))
                                            .describedAs("can load classloader types")
                                            .isNotNull();
                                } catch (ClassNotFoundException cnfe) {
                                    Assertions.fail("should not have raised a CNFE", cnfe);
                                }
                                executed.set(true);
                            }
                        });

        // then
        assertThat(executed.get()).isEqualTo(true);
    }

    @Test
    public void cannot_load_runnable_in_given_classloader_if_some_type_cant_be_loaded()
            throws Exception {
        // given
        final ClassLoader cl =
                isolatedClassLoader()
                        .withCurrentCodeSourceUrls()
                        .withPrivateCopyOf(ClassLoadersTest.class.getPackage().getName())
                        .without(AClass.class.getName())
                        .build();

        // when
        try {
            ClassLoaders.using(cl)
                    .execute(
                            new Runnable() {
                                @Override
                                public void run() {
                                    AClass cant_be_found = new AClass();
                                }
                            });
            Assertions.fail("should have raised a ClassNotFoundException");
        } catch (IllegalStateException ise) {
            // then
            assertThat(ise)
                    .hasCauseInstanceOf(NoClassDefFoundError.class)
                    .hasMessageContaining("AClass");
        }
    }

    @SuppressWarnings("unused")
    static class AClass {}

    @SuppressWarnings("unused")
    static class ClassUsingInterface1 implements Interface1 {}

    @SuppressWarnings("unused")
    interface Interface1 {}
}
