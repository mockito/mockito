package org.mockito.internal.creation.instance;

import java.lang.reflect.Constructor;

public class ConstructorInstantiator implements Instantiator {

    private final Object outerClassInstance;

    public ConstructorInstantiator(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }

    public <T> T newInstance(Class<T> cls) {
        try {
            if (outerClassInstance == null) {
                return cls.newInstance();
            }
            Constructor<T> c = cls.getDeclaredConstructor(outerClassInstance.getClass());
            return c.newInstance(outerClassInstance);
        } catch (Exception e) {
            throw new InstantationException("Unable to create mock instance of '"
                    + cls.getSimpleName() + "'.\nPlease ensure it has parameter-less constructor.", e);
        }
    }
}
