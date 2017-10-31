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
        return argument.equals(value);
    }
}
