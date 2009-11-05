package org.mockito.internal.creation;

import org.mockito.internal.creation.cglib.MockitoNamingPolicy;

public interface MockitoMethodProxy {

    Object invokeSuper(Object target, Object[] arguments) throws Throwable;

    void setNamingPolicyField(MockitoNamingPolicy namingPolicy);

}