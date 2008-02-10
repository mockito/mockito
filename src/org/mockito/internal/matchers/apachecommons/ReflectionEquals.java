/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.apachecommons;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;


public class ReflectionEquals extends ArgumentMatcher<Object>{
    private final Object wanted;

    public ReflectionEquals(Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(Object actual) {
        return EqualsBuilder.reflectionEquals(wanted, actual);
    }

    public void describeTo(Description description) {
        description.appendText("refEq(" + wanted + ")");
    }
}