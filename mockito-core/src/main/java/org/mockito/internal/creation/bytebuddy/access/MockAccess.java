/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy.access;

import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;

public interface MockAccess {

    MockMethodInterceptor getMockitoInterceptor();

    void setMockitoInterceptor(MockMethodInterceptor mockMethodInterceptor);
}
