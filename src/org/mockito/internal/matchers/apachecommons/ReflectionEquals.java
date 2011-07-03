/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers.apachecommons;

import java.io.Serializable;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

public class ReflectionEquals extends ArgumentMatcher<Object> implements Serializable {
    private static final long serialVersionUID = 2022780425116330014L;
    private final Object wanted;
    private final String[] excludeFields;

    public ReflectionEquals(Object wanted, String... excludeFields) {
        this.wanted = wanted;
        this.excludeFields = excludeFields;
    }

    public boolean matches(Object actual) {
        return EqualsBuilder.reflectionEquals(wanted, actual, excludeFields);
    }

    public void describeTo(Description description) {
        description.appendText("refEq(" + wanted + ")");
    }
}