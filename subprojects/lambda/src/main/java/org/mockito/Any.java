/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

public class Any<T> implements LambdaArgumentMatcher<T> {
    @Override
    public boolean matches(T argument) {
        return true;
    }
}
