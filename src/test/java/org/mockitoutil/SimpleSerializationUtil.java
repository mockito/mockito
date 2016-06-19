package org.mockitoutil;

import java.io.*;

import static junit.framework.TestCase.assertNotNull;

public abstract class SimpleSerializationUtil {

    //TODO use widely
    @SuppressWarnings("unchecked")
    public static <T> T serializeAndBack(T obj) throws Exception {
        ByteArrayOutputStream os = serializeMock(obj);
        return (T) deserializeMock(os, Object.class);
    }

    public static <T> T deserializeMock(ByteArrayOutputStream serialized, Class<T> type) throws IOException,
            ClassNotFoundException {
        InputStream unserialize = new ByteArrayInputStream(serialized.toByteArray());
        return deserializeMock(unserialize, type);
    }

    public static <T> T deserializeMock(InputStream unserialize, Class<T> type) throws IOException, ClassNotFoundException {
        Object readObject = new ObjectInputStream(unserialize).readObject();
        assertNotNull(readObject);
        return type.cast(readObject);
    }

    public static ByteArrayOutputStream serializeMock(Object mock) throws IOException {
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();
        new ObjectOutputStream(serialized).writeObject(mock);
        return serialized;
    }
}
