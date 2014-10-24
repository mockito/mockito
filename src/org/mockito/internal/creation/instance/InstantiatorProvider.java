package org.mockito.internal.creation.instance;

public class InstantiatorProvider {

    private final static Instantiator INSTANCE = new ObjenesisInstantiator();

    public Instantiator getInstantiator() {
        return INSTANCE;
    }
}
