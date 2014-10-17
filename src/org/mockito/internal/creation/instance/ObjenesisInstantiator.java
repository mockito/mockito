package org.mockito.internal.creation.instance;

import org.mockito.internal.configuration.GlobalConfiguration;
import org.objenesis.ObjenesisStd;

class ObjenesisInstantiator implements Instantiator {

    //TODO: in order to provide decent exception message when objenesis is not found,
    //have a constructor in this class that tries to instantiate ObjenesisStd and if it fails then show decent exception that dependency is missing
    //TODO: for the same reason catch and give better feedback when hamcrest core is not found.
    private final ObjenesisStd objenesis = new ObjenesisStd(new GlobalConfiguration().enableClassCache());

    public <T> T newInstance(Class<T> cls) {
        return objenesis.newInstance(cls);
    }
}
