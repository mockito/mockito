/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.invocation.Location;

@SuppressWarnings("unchecked")
public class LocalizedMatcher {

    private final ArgumentMatcher<?> matcher;
    private final Location location;

    public LocalizedMatcher(ArgumentMatcher<?> matcher) {
        this.matcher = matcher;
        this.location = new LocationImpl();
    }

    public Location getLocation() {
        return location;
    }

    public ArgumentMatcher<?> getMatcher() {
        return matcher;
    }
}