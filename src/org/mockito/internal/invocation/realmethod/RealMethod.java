package org.mockito.internal.invocation.realmethod;

public interface RealMethod {

    Object invoke(Object target, Object[] arguments) throws Throwable;

}
