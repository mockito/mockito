package org.mockito.internal.util.reflection;

import java.lang.reflect.Constructor;

public abstract class Constructors {

    /**
     * Returns the no arg constructor of the type if any.
     *
     * @param classToMock The type to look for a no-arg constructor
     * @return The no-arg constructor or null if none is declared.
     */
    public static Constructor<?> noArgConstructorOf(Class<?> classToMock) {
        try {
            return classToMock.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
