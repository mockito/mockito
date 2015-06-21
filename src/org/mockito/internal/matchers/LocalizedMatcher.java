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
public class LocalizedMatcher implements MockitoMatcher, ContainsExtraTypeInfo, CapturesArguments, MatcherDecorator, Serializable {

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

    public String toString() {
        return actualMatcher.toString();
    }

    public Location getLocation() {
        return location;
    }
    
    public String toStringWithType() {
        if (actualMatcher instanceof ContainsExtraTypeInfo) {
            return ((ContainsExtraTypeInfo) actualMatcher).toStringWithType();
        } else {
            return actualMatcher.toString();
        }
    }

    public boolean typeMatches(Object target) {
        return actualMatcher instanceof ContainsExtraTypeInfo
                && ((ContainsExtraTypeInfo) actualMatcher).typeMatches(target);
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