package org.mockitoutil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Assert;

public abstract class SimpleSerializationUtil {

    //TODO use widely
    @SuppressWarnings("unchecked")
    public static <T> T serializeAndBack(final T obj) throws Exception {
        final ByteArrayOutputStream os = serializeMock(obj);
        return (T) deserializeMock(os, Object.class);
    }

    public static <T> T deserializeMock(final ByteArrayOutputStream serialized, final Class<T> type) throws IOException,
            ClassNotFoundException {
        final InputStream unserialize = new ByteArrayInputStream(serialized.toByteArray());
        return deserializeMock(unserialize, type);
    }

    public static <T> T deserializeMock(final InputStream unserialize, final Class<T> type) throws IOException, ClassNotFoundException {
        final Object readObject = new ObjectInputStream(unserialize).readObject();
        Assert.assertNotNull(readObject);
        return type.cast(readObject);
    }

    public static ByteArrayOutputStream serializeMock(final Object mock) throws IOException {
        final ByteArrayOutputStream serialized = new ByteArrayOutputStream();
        new ObjectOutputStream(serialized).writeObject(mock);
        return serialized;
    }
}
