package org.mockito.internal.creation.bytebuddy;

public interface MockAccess {

    MockMethodInterceptor getMockitoInterceptor();

    void setMockitoInterceptor(MockMethodInterceptor mockMethodInterceptor);
}
