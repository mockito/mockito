package org.mockito.internal.creation.instance;

public class CachingObjenesisInstantiator implements Instantiator {

    private static ObjenesisInstantiator INSTANCE = new ObjenesisInstantiator();

    public <T> T newInstance(Class<T> cls) {
        return INSTANCE.newInstance(cls);
    }
}
