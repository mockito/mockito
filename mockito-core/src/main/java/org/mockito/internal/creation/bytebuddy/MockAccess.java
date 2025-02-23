/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

public interface MockAccess {

    MockMethodInterceptor getMockitoInterceptor();

    void setMockitoInterceptor(MockMethodInterceptor mockMethodInterceptor);
}
