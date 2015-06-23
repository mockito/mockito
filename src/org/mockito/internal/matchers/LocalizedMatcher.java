/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.util.text.HamcrestPrinter;
import org.mockito.invocation.Location;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class LocalizedMatcher implements Matcher, ContainsTypedDescription, CapturesArguments, MatcherDecorator, Serializable {

    private static final long serialVersionUID = 6748641229659825725L;
    private final Matcher actualMatcher;
    private final Location location;

    public LocalizedMatcher(Matcher actualMatcher) {
        this.actualMatcher = actualMatcher;
        this.location = new LocationImpl();
    }

    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        // yeah right
    }

    public boolean matches(Object item) {
        return actualMatcher.matches(item);
    }
    
    public void describeMismatch(Object item, Description description) {
        actualMatcher.describeMismatch(item, description);
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

    public String getTypedDescription() {
        if (actualMatcher instanceof ContainsTypedDescription) {
            return ((ContainsTypedDescription) actualMatcher).getTypedDescription();
        } else {
            return HamcrestPrinter.print(actualMatcher);
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
    public Matcher getActualMatcher() {
        return actualMatcher;
    }
}
