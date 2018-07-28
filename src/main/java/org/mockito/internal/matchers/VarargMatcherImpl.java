/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

/**
 * This class is used to verify with a vararg matcher
 */
public class VarargMatcherImpl<T> implements VarargMatcher, ArgumentMatcher<T> {
    private T[] values;
    private int iterator;
    public VarargMatcherImpl(T... value) {
        this.values = value;
    }

    @Override
    public boolean matches(T argument){
        iterator %= values.length;
        return values[iterator++].equals(argument);
    }

    public String toString() {
        String name = values.getClass().getComponentType().getSimpleName();
        String str = "vararg<"+name+">(";
        for(int i = 0 ; i < values.length; i++)
            str += values[i] + (i < values.length - 1 ? ", " : ")");
        return str;
    }
}
