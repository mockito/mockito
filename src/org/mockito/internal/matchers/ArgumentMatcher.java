/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

/**
 * Decides whether an actual argument is accepted.
 */
public interface ArgumentMatcher<T> {
    
    /**
     * Returns whether this matcher accepts the given argument. 
     * <p>
     * Like Object.equals(), it should be aware that the argument passed might 
     * be null and of any type. So you will usually start the method with an 
     * instanceof and/or null check.
     * <p>
     * The method should <b>never</b> assert if the argument doesn't match. It
     * should only return false.
     * 
     * @param argument the argument
     * @return whether this matcher accepts the given argument.
     */
    boolean matches(T argument);

    /**
     * Appends a string representation of this matcher to the given builder. In case
     * of failure, the printed message will show this string to allow to know which
     * matcher was used for the failing call.
     * 
     * @param builder the builder to which the string representation is appended.
     */
    void appendTo(StringBuilder builder);
}
