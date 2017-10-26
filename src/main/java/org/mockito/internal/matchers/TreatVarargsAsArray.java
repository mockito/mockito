/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.MatcherToString;
import java.io.Serializable;

/**
 * Wraps an {@link ArgumentMatcher} and indicates that it should be passed the varargs as a single
 * array rather than individual values.
 */
public class TreatVarargsAsArray<T>
    implements ArgumentMatcher<T>, ContainsExtraTypeInfo, CapturesArguments, Serializable {

    private final ArgumentMatcher<? super T> delegate;

    public TreatVarargsAsArray(ArgumentMatcher<? super T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean matches(T argument) {
        return delegate.matches(argument);
    }

    @Override
    public void captureFrom(Object argument) {
        if (delegate instanceof CapturesArguments) {
            ((CapturesArguments) delegate).captureFrom(argument);
        }
    }

    @Override
    public String toStringWithType() {
        if (delegate instanceof ContainsExtraTypeInfo) {
            String delagateAsString = ((ContainsExtraTypeInfo) delegate).toStringWithType();
            return wrapString(delagateAsString);
        }
        return toString();
    }

    @Override
    public String toString() {
        String delegateAsString = MatcherToString.toString(delegate);
        return wrapString(delegateAsString);
    }

    private String wrapString(String delegateAsString) {
        return "varargsAsArray(" + delegateAsString + ")";
    }

    @Override
    public boolean typeMatches(Object target) {
        if (delegate instanceof ContainsExtraTypeInfo) {
            return ((ContainsExtraTypeInfo) delegate).typeMatches(target);
        }
        return true;
    }
}
