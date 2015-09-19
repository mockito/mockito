/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.ValuePrinter;

import java.io.Serializable;

public class Equals implements ArgumentMatcher<Object>, ContainsExtraTypeInfo, Serializable {

    private final Object wanted;

    public Equals(Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(Object actual) {
        return Equality.areEqual(this.wanted, actual);
    }

    public String toString() {
        return describe(wanted);
    }

    private String describe(Object object) {
        return ValuePrinter.print(object);
    }

    protected final Object getWanted() {
        return wanted;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }
        Equals other = (Equals) o;
        return this.wanted == null && other.wanted == null || this.wanted != null && this.wanted.equals(other.wanted);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public String toStringWithType() {
        return "("+ wanted.getClass().getSimpleName() +") " + describe(wanted);
    }

    public boolean typeMatches(Object target) {
        return wanted != null && target != null && target.getClass() == wanted.getClass();
    }
}