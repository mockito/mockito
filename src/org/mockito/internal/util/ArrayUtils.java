package org.mockito.internal.util;

public class ArrayUtils {
    public Class<?>[] concat(Class<?>[] interfaces, Class<?> clazz) {
        int length = interfaces.length;
        Class[] out = new Class[length +1];
        System.arraycopy(interfaces, 0, out, 0, length);
        out[length] = clazz;
        return out;
    }
}