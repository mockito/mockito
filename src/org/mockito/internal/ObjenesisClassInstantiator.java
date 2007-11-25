/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.objenesis.ObjenesisHelper;

public class ObjenesisClassInstantiator {

    public static Object newInstance(Class<?> clazz) throws InstantiationException {
        return ObjenesisHelper.newInstance(clazz);
    }
}
