package org.mockito.internal.creation.instance;

import org.mockito.mock.MockCreationSettings;

@SuppressWarnings("rawtypes")
public class InstantiatorProvider {

    private static final Instantiator INSTANCE = new ObjenesisInstantiator();

    public Instantiator getInstantiator(final MockCreationSettings settings) {
        if (settings.isUsingConstructor()) {
            return new ConstructorInstantiator(settings.getOuterClassInstance());
        } else {
            return INSTANCE;
        }
    }
}