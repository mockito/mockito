package org.mockito;

public interface LambdaArgumentMatcher<T> extends ArgumentMatcher<T> {

    default T getValue() {
        return null;
    }
}
