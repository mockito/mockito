/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.util;

//TODO SF Replace with RealMethod and get rid of (possibly).
public interface MockitoMethodProxy {
    Object invokeSuper(Object target, Object[] arguments);
}