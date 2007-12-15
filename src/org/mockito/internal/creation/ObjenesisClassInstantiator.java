/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.objenesis.ObjenesisHelper;

public class ObjenesisClassInstantiator {

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class clazz) throws InstantiationException {
        return ObjenesisHelper.newInstance(clazz);
    }
}
