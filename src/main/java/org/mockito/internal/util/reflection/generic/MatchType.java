package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public interface MatchType extends Type {

    boolean matches(MatchType other);

    static MatchType ofClassAndResolver(Class<?> clazz, VariableResolver resolver) {
        TypeVariable<? extends Class<?>>[] parameters = clazz.getTypeParameters();
        if (parameters.length > 0) {
            return MatchParameterizedClass.ofClassAndResolver(clazz, resolver);
        }
        else if (clazz.isArray()) {
            return MatchArrayClass.ofClassAndResolver(clazz, resolver);
        }
        else {
            return MatchClass.ofClass(clazz);
        }
    }

}
