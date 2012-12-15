package org.mockitousage.serialization;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.SimplePerRealmReloadingClassLoader;
import org.mockitoutil.SimpleSerializationUtil;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Callable;


public class AcrossClassLoaderSerializationTest {

    @Test
    public void check_that_mock_can_be_serialized_in_a_classloader_and_deserialized_in_another() throws Exception {
        byte[] bytes = create_mock_and_serialize_it_in_class_loader_A();

        Object the_deserialized_mock = read_stream_and_deserialize_it_in_class_loader_B(bytes);
    }

    private Object read_stream_and_deserialize_it_in_class_loader_B(byte[] bytes) throws Exception {
        return new SimplePerRealmReloadingClassLoader(this.getClass().getClassLoader(), isolating_test_classes())
                .doInRealm(
                        "org.mockitousage.serialization.AcrossClassLoaderSerializationTest$ReadStreamAndDeserializeIt",
                        new Class[]{ byte[].class },
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
                        || qualifiedName.contains("org.mockitoutil");
            }
        };
    }


    // see create_mock_and_serialize_it_in_class_loader_A
    public static class CreateMockAndSerializeIt implements Callable<byte[]> {
        public byte[] call() throws Exception {
            IMethods mock = Mockito.mock(IMethods.class, Mockito.withSettings().serializable());
            // use MethodProxy before
            mock.linkedListReturningMethod();

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
            ByteArrayInputStream unserialize = new ByteArrayInputStream(bytes);
            return SimpleSerializationUtil.deserializeMock(unserialize, IMethods.class);
        }
    }



}
