/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;

import org.easymock.ArgumentsMatcher;

public interface ILegacyMatcherMethods {

    void setDefaultMatcher(ArgumentsMatcher matcher);

    void setMatcher(Method method, ArgumentsMatcher matcher);
}