package org.mockito.internal.creation.instance;

public class CachingObjenesisInstanceFactory implements InstanceFactory {

    private static ObjenesisInstanceFactory INSTANCE = new ObjenesisInstanceFactory();

    public <T> T newInstance(Class<T> cls) {
        return INSTANCE.newInstance(cls);
    }
}
