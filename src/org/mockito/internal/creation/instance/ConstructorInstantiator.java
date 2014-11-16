package org.mockito.internal.creation.instance;

import java.lang.reflect.Constructor;

public class ConstructorInstantiator implements Instantiator {

    private final Object outerClassInstance;

    public ConstructorInstantiator(Object outerClassInstance) {
        this.outerClassInstance = outerClassInstance;
    }

    public <T> T newInstance(Class<T> cls) {
        if (outerClassInstance == null) {
            return noArgConstructor(cls);
        }
        return withOuterClass(cls);
    }

    private <T> T withOuterClass(Class<T> cls) {
        try {
            Constructor<T> c = cls.getDeclaredConstructor(outerClassInstance.getClass());
            return c.newInstance(outerClassInstance);
        } catch (Exception e) {
            throw new InstantationException("Unable to create mock instance of '"
                    + cls.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e);
        }
    }

    private <T> T noArgConstructor(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            throw new InstantationException("Unable to create mock instance of '"
                    + cls.getSimpleName() + "'.\nPlease ensure it has parameter-less constructor.", e);
        }
    }
}
