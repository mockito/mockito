/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;

public class LegacyMatcherProvider {

    private ArgumentsMatcher defaultMatcher;

    private boolean defaultMatcherSet;

    private Map<Method, ArgumentsMatcher> matchers = new HashMap<Method, ArgumentsMatcher>();

    public ArgumentsMatcher getMatcher(Method method) {
        if (!matchers.containsKey(method)) {
            if (!defaultMatcherSet) {
                setDefaultMatcher(MockControl.EQUALS_MATCHER);
            }
            matchers.put(method, defaultMatcher);
        }
        return matchers.get(method);
    }

    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        if (defaultMatcherSet) {
            throw new RuntimeExceptionWrapper(
                    new IllegalStateException(
                            "default matcher can only be set once directly after creation of the MockControl"));
        }
        defaultMatcher = matcher;
        defaultMatcherSet = true;
    }

    public void setMatcher(Method method, ArgumentsMatcher matcher) {
        if (matchers.containsKey(method) && matchers.get(method) != matcher) {
            throw new RuntimeExceptionWrapper(new IllegalStateException(
                    "for method "
                            + method.getName()
                            + "("
                            + (method.getParameterTypes().length == 0 ? ""
                                    : "...")
                            + "), a matcher has already been set"));
        }
        matchers.put(method, matcher);
    }
}
