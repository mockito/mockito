/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

/**
 * Intended to use in certain ArgumentMatchers.
 * When ArgumentMatcher fails, chance is that the actual object has the same output of toString() than
 * the wanted object. This looks weird when failures are reported.
 * Therefore when matcher fails but toString() yields the same outputs,
 * we will try to use the {@link #toStringWithType(boolean)} method.
 */
public interface ContainsExtraTypeInfo {

    /**
     * @param useFullyQualifiedClassName - uses fully qualified class name if true else simple class name
     * Returns more verbose description of the object which include type information
     */
    String toStringWithType(boolean useFullyQualifiedClassName);

    /**
     * Checks if target target has matching type.
     * If the type matches, there is no point in rendering result from {@link #toStringWithType(boolean)}
     */
    boolean typeMatches(Object target);

    /**
     *
     * @return Returns the Class of the argument
     */
    Class getWantedClass();
}
