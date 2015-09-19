package org.mockitoutil;

import junit.framework.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class SimpleSerializationUtil {

    //TODO use widely
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
        Assert.assertNotNull(readObject);
        return type.cast(readObject);
    }

    public static ByteArrayOutputStream serializeMock(Object mock) throws IOException {
        ByteArrayOutputStream serialized = new ByteArrayOutputStream();
        new ObjectOutputStream(serialized).writeObject(mock);
        return serialized;
    }
}
