package org.mockito.internal;


public interface MockitoExpectation<T> {

    void andReturn(T value);

    void andThrows(Throwable throwable);
}