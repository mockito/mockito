package org.mockito.internal.creation.instance;

import org.mockito.mock.MockCreationSettings;

public class InstantiatorProvider {

    private final static Instantiator INSTANCE = new ObjenesisInstantiator();

    public Instantiator getInstantiator(MockCreationSettings settings) {
        if (settings.isUsingConstructor()) {
            return new ConstructorInstantiator(settings.getOuterClassInstance());
        } else {
            return INSTANCE;
        }
    }
}