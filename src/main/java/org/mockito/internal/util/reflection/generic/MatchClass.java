package org.mockito.internal.util.reflection.generic;

public class MatchClass implements MatchType, HasClass {

    private final Class<?> clazz;

    protected MatchClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean matches(MatchType other) {
        return other instanceof MatchClass && this.clazz.isAssignableFrom(((MatchClass)other).clazz);
    }

    static MatchType ofClass(Class<?> clazz) {
        return new MatchClass(clazz);
    }

    @Override
    public Class<?> getTheClass() {
        return clazz;
    }
}
