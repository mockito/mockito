package org.mockito.internal.creation.instance;

import java.lang.reflect.Constructor;

public class ConstructorInstantiator implements Instantiator {

    private final Object outerClassInstance;

    public ConstructorInstantiator(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }

    public <T> T newInstance(Class<T> cls) {
        try {
            if (outerClassInstance != null) {
                Constructor<T> c = cls.getDeclaredConstructor(outerClassInstance.getClass());
                return c.newInstance(outerClassInstance);
            }
            return cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Problems instantiating class: " + cls, e);
        }
    }
}
