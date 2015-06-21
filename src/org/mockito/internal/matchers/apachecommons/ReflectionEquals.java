/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers.apachecommons;

import org.mockito.MockitoMatcher;

import java.io.Serializable;

public class ReflectionEquals extends MockitoMatcher<Object> implements Serializable {

    private final Object wanted;
    private final String[] excludeFields;

    public ReflectionEquals(Object wanted, String... excludeFields) {
        this.wanted = wanted;
        this.excludeFields = excludeFields;
    }

    public boolean matches(Object actual) {
        return EqualsBuilder.reflectionEquals(wanted, actual, excludeFields);
    }

    public String describe() {
        return "refEq(" + wanted + ")";
    }
}