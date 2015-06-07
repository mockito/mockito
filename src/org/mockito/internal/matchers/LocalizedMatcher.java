/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.invocation.Location;

@SuppressWarnings("rawtypes")
public class LocalizedMatcher implements Matcher, ContainsExtraTypeInformation, CapturesArguments, MatcherDecorator, Serializable {

    private static final long serialVersionUID = 6748641229659825725L;
    private final Matcher actualMatcher;
    private final Location location;

    public LocalizedMatcher(final Matcher actualMatcher) {
        this.actualMatcher = actualMatcher;
        this.location = new LocationImpl();
    }

    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        // yeah right
    }

    public boolean matches(final Object item) {
        return actualMatcher.matches(item);
    }

    public void describeTo(final Description description) {
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

    public boolean typeMatches(final Object object) {
        return actualMatcher instanceof ContainsExtraTypeInformation
                && ((ContainsExtraTypeInformation) actualMatcher).typeMatches(object);
    }

    public void captureFrom(final Object argument) {
        if (actualMatcher instanceof CapturesArguments) {
            ((CapturesArguments) actualMatcher).captureFrom(argument);
        }
    }

    //TODO: refactor other 'delegated interfaces' to use the MatcherDecorator feature
    public Matcher getActualMatcher() {
        return actualMatcher;
    }
}