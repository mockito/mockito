/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.mockito.internal.debugging.Location;

@SuppressWarnings("unchecked")
public class LocalizedMatcher implements Matcher, ContainsExtraTypeInformation, CapturesArguments {

    private final Matcher actualMatcher;
    private Location location;

    public LocalizedMatcher(Matcher actualMatcher) {
        this.actualMatcher = actualMatcher;
        this.location = new Location();
    }

    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        // yeah right...
    }

    public boolean matches(Object item) {
        return actualMatcher.matches(item);
    }

    public void describeTo(Description description) {
        actualMatcher.describeTo(description);
    }

    public Location getLocation() {
        return location;
    }
    
    @Override
    public String toString() {
        return "Localized: " + this.actualMatcher;
    }

    public SelfDescribing withExtraTypeInfo() {
        if (actualMatcher instanceof ContainsExtraTypeInformation) {
            return ((ContainsExtraTypeInformation) actualMatcher).withExtraTypeInfo();
        } else {
            return this;
        }
    }

    public boolean typeMatches(Object object) {
        if (actualMatcher instanceof ContainsExtraTypeInformation) {
            return ((ContainsExtraTypeInformation) actualMatcher).typeMatches(object);
        } else {
            return false;
        }
    }

    public void captureFrom(Object argument) {
        if (actualMatcher instanceof CapturesArguments) {
            ((CapturesArguments) actualMatcher).captureFrom(argument);
        }
    }
}