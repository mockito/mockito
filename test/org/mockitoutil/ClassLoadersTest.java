package org.mockitoutil;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
import static org.mockitoutil.ClassLoaders.isolatedClassLoader;
import org.junit.Test;

public class ClassLoadersTest {

    public static final String CLASS_NAME_USING_INTERFACE = "org.mockitoutil.ClassLoadersTest$ClassUsingInterface1";
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
    public void isolated_class_loader_cannot_load_classes_if_no_code_source_path() throws Exception {
        // given
        ClassLoader cl = isolatedClassLoader()
                .withPrivateCopyOf(CLASS_NAME_USING_INTERFACE)
                .build();

        // when
        try {
            cl.loadClass(CLASS_NAME_USING_INTERFACE);
            fail();
        } catch (ClassNotFoundException e) {
            // then
            assertThat(e.getMessage()).contains(CLASS_NAME_USING_INTERFACE);
        }
    }

    @Test
    public void isolated_class_loader_cannot_load_classes_not_matching_the_prefix() throws Exception {
        // given
        ClassLoader cl = isolatedClassLoader()
                .withCurrentCodeSourceUrls()
                .withPrivateCopyOf(CLASS_NAME_USING_INTERFACE)
                .build();

        // when
        try {
            cl.loadClass(CLASS_NAME_USING_INTERFACE);
            fail();
        } catch (NoClassDefFoundError e) {
            // then
            assertThat(e.getMessage()).contains("org/mockitoutil/ClassLoadersTest$Interface1");
        }
    }

    @Test
    public void isolated_class_loader_can_load_all_classes_unless_all_classes_mathch_the_prefixes() throws Exception {
        // given
        ClassLoader cl = isolatedClassLoader()
                .withCurrentCodeSourceUrls()
                .withPrivateCopyOf(CLASS_NAME_USING_INTERFACE)
                .withPrivateCopyOf(INTERFACE_NAME)
                .build();

        // when
        Class<?> aClass = cl.loadClass(CLASS_NAME_USING_INTERFACE);

        // then
        assertThat(aClass).isNotNull();
        assertThat(aClass.getClassLoader()).isEqualTo(cl);
        assertThat(aClass.getInterfaces()[0].getClassLoader()).isEqualTo(cl);
    }

    @Test
    public void isolated_class_loader_has_no_parent() throws Exception {
        ClassLoader cl = isolatedClassLoader()
                .withCurrentCodeSourceUrls()
                .withPrivateCopyOf(CLASS_NAME_USING_INTERFACE)
                .withPrivateCopyOf(INTERFACE_NAME)
                .build();

        assertThat(cl.getParent()).isNull();
    }


    @Test
    public void can_not_load_a_class_not_previously_registered_in_builder() throws Exception {
        // given
        ClassLoader cl = ClassLoaders
                .inMemoryClassLoader()
                .withClassDefinition("yop.Dude", SimpleClassGenerator.makeMarkerInterface("yop.Dude"))
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
        ClassLoader cl = ClassLoaders
                .inMemoryClassLoader()
                .withClassDefinition("yop.Dude", SimpleClassGenerator.makeMarkerInterface("yop.Dude"))
                .build();

        // when
        Class<?> aClass = cl.loadClass("yop.Dude");

        // then
        assertThat(aClass).isNotNull();
        assertThat(aClass.getClassLoader()).isEqualTo(cl);
        assertThat(aClass.getName()).isEqualTo("yop.Dude");
    }

    static class ClassUsingInterface1 implements Interface1 { }
    interface Interface1 { }
}
