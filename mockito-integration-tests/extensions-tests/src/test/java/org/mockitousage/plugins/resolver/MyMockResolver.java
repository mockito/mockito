/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.resolver;

import org.mockito.plugins.MockResolver;

public class MyMockResolver implements MockResolver {

    @Override
    public Object resolve(Object instance) {
        if (instance instanceof MockResolverTest.MockWrapper) {
            return ((MockResolverTest.MockWrapper) instance).getMock();
        }
        return instance;
    }
}
