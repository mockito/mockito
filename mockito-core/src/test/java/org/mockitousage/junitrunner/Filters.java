/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

public class Filters {
    public static Filter methodNameContains(final String substring) {
        return new Filter() {
            @Override
            public boolean shouldRun(Description description) {
                return description.getDisplayName().contains(substring);
            }

            @Override
            public String describe() {
                return null;
            }
        };
    }
}
