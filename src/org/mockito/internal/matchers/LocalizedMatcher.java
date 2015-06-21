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
public class LocalizedMatcher extends MockitoMatcher implements ContainsTypedDescription, CapturesArguments, MatcherDecorator, Serializable {

    private final MockitoMatcher actualMatcher;
    private final Location location;

    //TODO SF why does it extend the MockitoMatcher and yet implement MatcherDecorator?
    public LocalizedMatcher(MockitoMatcher actualMatcher) {
        this.actualMatcher = actualMatcher;
        this.location = new LocationImpl();
    }

    public boolean matches(Object item) {
        return actualMatcher.matches(item);
    }

    public String describe() {
        return actualMatcher.describe();
    }

    public Location getLocation() {
        return location;
    }
    
    @Override
    public String toString() {
        return "Localized: " + this.actualMatcher;
    }

    public String getTypedDescription() {
        if (actualMatcher instanceof ContainsTypedDescription) {
            return ((ContainsTypedDescription) actualMatcher).getTypedDescription();
        } else {
            return actualMatcher.describe();
        }
    }

    public boolean typeMatches(Object object) {
        return actualMatcher instanceof ContainsTypedDescription
                && ((ContainsTypedDescription) actualMatcher).typeMatches(object);
    }

    public void captureFrom(Object argument) {
        if (actualMatcher instanceof CapturesArguments) {
            ((CapturesArguments) actualMatcher).captureFrom(argument);
        }
    }

    //TODO: refactor other 'delegated interfaces' to use the MatcherDecorator feature
    public MockitoMatcher getActualMatcher() {
        return actualMatcher;
    }
}