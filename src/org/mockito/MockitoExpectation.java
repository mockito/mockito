package org.mockito;


public interface MockitoExpectation<T> {

    void andReturn(T value);

    void andThrows(Throwable throwable);
}