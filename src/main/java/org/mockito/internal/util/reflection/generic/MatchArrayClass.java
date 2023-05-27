package org.mockito.internal.util.reflection.generic;

public class MatchArrayClass extends MatchClass {

    private final MatchType componentMatchType;

    protected MatchArrayClass(Class<?> clazz, MatchType componentMatchType) {
        super(clazz);
        this.componentMatchType = componentMatchType;
    }

    @Override
    public boolean matches(MatchType other) {
        return super.matches(other) && other instanceof MatchArrayClass
            && componentMatchType.matches(((MatchArrayClass) other).componentMatchType);
    }

    static MatchType ofClassAndResolver(Class<?> clazz, VariableResolver resolver) {
        MatchType componentMatchType = MatchType.ofClassAndResolver(clazz.getComponentType(), resolver);
        return new MatchArrayClass(clazz, componentMatchType);
    }
}
