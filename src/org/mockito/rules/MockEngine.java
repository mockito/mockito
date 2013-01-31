package org.mockito.rules;

public interface MockEngine {

    <T> T mock(Class<T> type);

}
