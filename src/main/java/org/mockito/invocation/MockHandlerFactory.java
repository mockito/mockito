/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.mock.MockCreationSettings;

public interface MockHandlerFactory {

    public <T> MockHandler<T> createMockHandler(MockCreationSettings<T> settings);
}
