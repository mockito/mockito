package org.mockito.internal.creation.instance;

/**
 * Provides instances of classes.
 */
public interface InstanceFactory {

    /**
     * Creates instance of given class
     */
    <T> T newInstance(Class<T> cls);
}
