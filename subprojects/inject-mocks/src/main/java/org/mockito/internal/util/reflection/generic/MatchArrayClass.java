/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

public class MatchArrayClass extends MatchClass {

    private final MatchType componentMatchType;

    protected MatchArrayClass(Class<?> clazz, MatchType componentMatchType) {
        super(clazz);
        this.componentMatchType = componentMatchType;
    }

    @Override
    public boolean matchesSource(MatchType other) {
        return super.matchesSource(other)
                && other instanceof MatchArrayClass
                && componentMatchType.matchesSource(((MatchArrayClass) other).componentMatchType);
    }

    static MatchType ofClassAndResolver(
            GenericArrayType genericType, Class<?> clazz, VariableResolver resolver) {
        MatchType componentMatchType =
                MatchType.ofGenericAndRawTypeAndResolver(
                        genericType.getGenericComponentType(), clazz.getComponentType(), resolver);
        return new MatchArrayClass(clazz, componentMatchType);
    }
}
