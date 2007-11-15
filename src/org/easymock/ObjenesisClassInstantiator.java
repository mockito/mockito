package org.easymock;

import org.objenesis.ObjenesisHelper;

public class ObjenesisClassInstantiator {

    public static Object newInstance(Class clazz) throws InstantiationException {
        return ObjenesisHelper.newInstance(clazz);
    }
}
