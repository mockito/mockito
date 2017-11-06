/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

public class Equals<A> implements LambdaArgumentMatcher<A> {

    private final A value;

    Equals(A value) {
        this.value = value;
    }

    @Override
    public A getValue() {
        return value;
    }

    @Override
    public boolean matches(A argument) {
        return argument == value || argument != null && argument.equals(value);
    }
}
