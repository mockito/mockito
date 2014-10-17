package org.mockito.internal.creation.instance;

public class ConstructorInstanceFactory implements InstanceFactory {

    public <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Problems instantiating class: " + cls, e);
        }
    }
}
