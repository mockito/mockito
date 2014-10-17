package org.mockito.internal.creation.jmock;

import org.mockito.cglib.proxy.MethodInterceptor;

import java.util.Collection;

/**
 * Creates proxies of given classes
 */
public interface ClassImposterizer {

    <T> T imposterise(MethodInterceptor interceptor, Class<T> mockedType, Collection<Class> ancillaryTypes);

    <T> T imposterise(MethodInterceptor interceptor, Class<T> mockedType, Class<?>... ancillaryTypes);
}
