package org.mockito;

public interface VoidMethodExpectation<T> {

    MethodSelector<T> toThrow(Throwable throwable);

}
