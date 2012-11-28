package org.mockitousage.basicapi;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.SimplePerRealmReloadingClassLoader;
import org.mockitoutil.SimpleSerializationUtil;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;


/**
 * Doesn't work, for now. maybe with more hacky way to tamper the outputstream, still...
 *
 * While investigating, I discovered that <code>ObjectOutputStream</code> will write the real class in the stream
 * through <code>writeClass</code> before the <code>writeReplace</code> (see <code>writeObject0</code>), that means
 * that the CGLIB proxy class is written in the stream anyway, unless you don't use an ObjectOutputStream directly.
 *
 * So when one use the standard <code>ObjectInputStream</code> it will read the bytestream and use the written class
 * info to lookup for it in the classloader, unfortunately this class matches the class in another classloader and
 * isn't available anyway at deserialization time. Overriding <code>resolveClass</code> in a subclass of
 * <code>ObjectInoutStream</code> might help though to overcome this shortcomming.
 *
 *
 * Yet JDK proxies are serializable, how are they doing it !? Well <code>ObjectOutputStream</code> have specific
 * treatment for JDK proxy especially there <code>writeClassDesc</code> which uses an <code>ObjectStreamClass</code>
 * that is initialized in his constructor with <code>Proxy.isProxy</code>
 *
 * Note for the hacker, the class descriptor of a JDK Proxy is written with the following byte
 * <code>ObjectStreamConstants.TC_PROXYCLASSDESC</code> this byte in the stream will trigger the specific overridable
 * <code>resolveProxyClass</code> (see <code>readClassDesc</code>), though yet again it means the method has to be
 * overrided in the input stream.
 *
 * TODO for the hacker, not for real... See if it is possible to tamper the BlockDataOutputStream and then simulate a writeReplace
 * ... well probably don't worth it
 *
 */
public class AcrossClassLoaderSerializationTest {

    @Test
    public void name() throws Exception {
        byte[] bytes = create_mock_and_Serialize_it();

        System.out.println(new String(bytes));

        FileOutputStream outputStream = new FileOutputStream("acrossJVM", false);
        outputStream.write(bytes);
        outputStream.close();

        Object the_deserialized_mock = read_stream_and_deserialize_it(bytes);

        System.out.println(the_deserialized_mock);
    }

    private Object read_stream_and_deserialize_it(byte[] bytes) throws Exception {
        return new SimplePerRealmReloadingClassLoader(this.getClass().getClassLoader(), reload_all())
                .doInRealm(
                        "org.mockitousage.basicapi.AcrossClassLoaderSerializationTest$ReadStreanAndDeserializeIt",
                        new Class[]{ byte[].class },
                        new Object[]{ bytes }
                );
    }

    private byte[] create_mock_and_Serialize_it() throws Exception {
        return (byte[]) new SimplePerRealmReloadingClassLoader(this.getClass().getClassLoader(), reload_all())
                .doInRealm("org.mockitousage.basicapi.AcrossClassLoaderSerializationTest$CreateMockAndSerializeIt");
    }

    private SimplePerRealmReloadingClassLoader.ReloadClassPredicate reload_all() {
        return new SimplePerRealmReloadingClassLoader.ReloadClassPredicate() {
            public boolean acceptReloadOf(String qualifiedName) {
                return qualifiedName.contains("org.mockitousage")
//                        || qualifiedName.contains("org.mockito")
//                        || qualifiedName.contains("org.mockito.cglib")
                        || qualifiedName.contains("org.mockitoutil");
            }
        };
    }


    public static class CreateMockAndSerializeIt implements Callable<byte[]> {
        public byte[] call() throws Exception {
            IMethods mock = Mockito.mock(IMethods.class, Mockito.withSettings().serializable());
            // use MethodProxy before
//            mock.linkedListReturningMethod();

            return SimpleSerializationUtil.serializeMock(mock).toByteArray();
        }
    }


    public static class ReadStreanAndDeserializeIt implements Callable<Object> {
        private byte[] bytes;

        public ReadStreanAndDeserializeIt(byte[] bytes) {
            this.bytes = bytes;
        }

        public Object call() throws Exception {
            ByteArrayInputStream unserialize = new ByteArrayInputStream(bytes);
            return SimpleSerializationUtil.deserializeMock(unserialize, IMethods.class);
        }
    }



}
