package org.mockitoutil;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockitoutil.ClassLoaders.isolatedClassLoader;
import org.junit.Test;

public class ClassLoadersTest {

    @Test(expected = ClassNotFoundException.class)
    public void isolated_class_loader_cannot_load_classes_when_no_given_prefix() throws Exception {
        // given
        ClassLoader cl = isolatedClassLoader().build();

        // when
        cl.loadClass("org.mockito.Mockito");

        // then raises CNFE
    }

    @Test
    public void isolated_class_loader_cannot_load_classes_If_no_code_source_path() throws Exception {
        // given
        ClassLoader cl = isolatedClassLoader()
                .withPrivateCopyOf("org.mockitoutil.ClassLoadersTest$ClassUsingInterface1")
                .build();

        // when
        try {
            cl.loadClass("org.mockitoutil.ClassLoadersTest$ClassUsingInterface1");
        } catch (ClassNotFoundException e) {
            // then
            assertThat(e.getMessage()).contains("org.mockitoutil.ClassLoadersTest$ClassUsingInterface1");
        }
    }

    @Test
    public void isolated_class_loader_cannot_load_classes_that_require_other_non_declared_classes() throws Exception {
        // given
        ClassLoader cl = isolatedClassLoader()
                .withCurrentCodeSourceUrls()
                .withPrivateCopyOf("org.mockitoutil.ClassLoadersTest$ClassUsingInterface1")
                .build();

        // when
        try {
            cl.loadClass("org.mockitoutil.ClassLoadersTest$ClassUsingInterface1");
        } catch (NoClassDefFoundError e) {
            // then
            assertThat(e.getMessage()).contains("org/mockitoutil/ClassLoadersTest$Interface1");
        }
    }

    @Test
    public void isolated_class_loader_can_load_all_classes_if_all_prefixes_are_correct() throws Exception {
        // given
        ClassLoader cl = isolatedClassLoader()
                .withCurrentCodeSourceUrls()
                .withPrivateCopyOf("org.mockitoutil.ClassLoadersTest$ClassUsingInterface1")
                .withPrivateCopyOf("org.mockitoutil.ClassLoadersTest$Interface1")
                .build();

        // when
        Class<?> aClass = cl.loadClass("org.mockitoutil.ClassLoadersTest$ClassUsingInterface1");

        // then
        assertThat(aClass).isNotNull();
        assertThat(aClass.getClassLoader()).isEqualTo(cl);
        assertThat(aClass.getInterfaces()[0].getClassLoader()).isEqualTo(cl);
    }

    @Test
    public void isolated_class_loader_has_no_parent() throws Exception {
        ClassLoader cl = isolatedClassLoader()
                .withCurrentCodeSourceUrls()
                .withPrivateCopyOf("org.mockitoutil.ClassLoadersTest$ClassUsingInterface1")
                .withPrivateCopyOf("org.mockitoutil.ClassLoadersTest$Interface1")
                .build();

        assertThat(cl.getParent()).isNull();
    }


    static class ClassUsingInterface1 implements Interface1 { }
    interface Interface1 { }
}
