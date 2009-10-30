package org.mockito.internal.matchers;

import org.hamcrest.Matcher;
import org.mockito.ArgumentMatcher;

@SuppressWarnings("unchecked")
public class AnyVararg extends ArgumentMatcher implements VarargMatcher {

    private static final long serialVersionUID = 1700721373094731555L;
    public static final Matcher ANY_VARARG = new AnyVararg();

    public boolean matches(Object arg) {
        return true;
    }
}