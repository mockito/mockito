package org.mockito.internal;


public interface VoidMethodExpectation<T> {

    MethodSelector<T> toThrow(Throwable throwable);

}
