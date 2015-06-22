/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockito.MockitoMatcher;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.invocation.Location;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class LocalizedMatcher {

    private final MockitoMatcher matcher;
    private final Location location;

    public LocalizedMatcher(MockitoMatcher matcher) {
        this.matcher = matcher;
        this.location = new LocationImpl();
    }

    public Location getLocation() {
        return location;
    }

    public MockitoMatcher getMatcher() {
        return matcher;
    }
}