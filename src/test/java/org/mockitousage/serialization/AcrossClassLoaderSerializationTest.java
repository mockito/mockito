/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.serialization;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.mock.SerializableMode;
import org.mockitousage.IMethods;
import org.mockitoutil.SimplePerRealmReloadingClassLoader;
import org.mockitoutil.SimpleSerializationUtil;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;

public class AcrossClassLoaderSerializationTest {

    public IMethods mock;

    @Before
    public void reproduce_CCE_by_creating_a_mock_with_IMethods_before() throws Exception {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void check_that_mock_can_be_serialized_in_a_classloader_and_deserialized_in_another() throws Exception {
        byte[] bytes = create_mock_and_serialize_it_in_class_loader_A();

        Object the_deserialized_mock = read_stream_and_deserialize_it_in_class_loader_B(bytes);
        assertThat(the_deserialized_mock.getClass().getName()).startsWith("org.mockito.codegen.AClassToBeMockedInThisTestOnlyAndInCallablesOnly");
    }

    private Object read_stream_and_deserialize_it_in_class_loader_B(byte[] bytes) throws Exception {
        return new SimplePerRealmReloadingClassLoader(this.getClass().getClassLoader(), isolating_test_classes())
                .doInRealm(
                        "org.mockitousage.serialization.AcrossClassLoaderSerializationTest$ReadStreamAndDeserializeIt",
                        new Class<?>[]{ byte[].class },
                        new Object[]{ bytes }
                );
    }

    private byte[] create_mock_and_serialize_it_in_class_loader_A() throws Exception {
        return (byte[]) new SimplePerRealmReloadingClassLoader(this.getClass().getClassLoader(), isolating_test_classes())
                .doInRealm("org.mockitousage.serialization.AcrossClassLoaderSerializationTest$CreateMockAndSerializeIt");
    }


    private SimplePerRealmReloadingClassLoader.ReloadClassPredicate isolating_test_classes() {
        return new SimplePerRealmReloadingClassLoader.ReloadClassPredicate() {
            public boolean acceptReloadOf(String qualifiedName) {
                return qualifiedName.contains("org.mockitousage")
                        || qualifiedName.contains("org.mockitoutil")
                        ;
            }
        };
    }


    // see create_mock_and_serialize_it_in_class_loader_A
    public static class CreateMockAndSerializeIt implements Callable<byte[]> {
        public byte[] call() throws Exception {
            AClassToBeMockedInThisTestOnlyAndInCallablesOnly mock = Mockito.mock(
                    AClassToBeMockedInThisTestOnlyAndInCallablesOnly.class,
                    Mockito.withSettings().serializable(SerializableMode.ACROSS_CLASSLOADERS)
            );
            // use MethodProxy before
            mock.returningSomething();

            return SimpleSerializationUtil.serializeMock(mock).toByteArray();
        }
    }

    // see read_stream_and_deserialize_it_in_class_loader_B
    public static class ReadStreamAndDeserializeIt implements Callable<Object> {
        private byte[] bytes;

        public ReadStreamAndDeserializeIt(byte[] bytes) {
            this.bytes = bytes;
        }

        public Object call() throws Exception {
            ByteArrayInputStream to_unserialize = new ByteArrayInputStream(bytes);
            return SimpleSerializationUtil.deserializeMock(
                    to_unserialize,
                    AClassToBeMockedInThisTestOnlyAndInCallablesOnly.class
            );
        }
    }


    public static class AClassToBeMockedInThisTestOnlyAndInCallablesOnly {
        List returningSomething() { return Collections.emptyList(); }
    }
}
