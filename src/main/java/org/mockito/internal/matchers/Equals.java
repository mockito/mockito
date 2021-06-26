/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.ValuePrinter;

public class Equals implements ArgumentMatcher<Object>, ContainsExtraTypeInfo, Serializable {

    private final Object wanted;

    public Equals(Object wanted) {
        this.wanted = wanted;
    }

    @Override
    public boolean matches(Object actual) {
        return Equality.areEqual(this.wanted, actual);
    }

    @Override
    public String toString() {
        return describe(wanted);
    }

    private String describe(Object object) {
        return ValuePrinter.print(object);
    }

    @Override
    public final Object getWanted() {
        return wanted;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Equals)) {
            return false;
        }
        Equals other = (Equals) o;
        return (this.wanted == null && other.wanted == null)
                || this.wanted != null && this.wanted.equals(other.wanted);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toStringWithType(String className) {
        return "(" + className + ") " + describe(wanted);
    }

    @Override
    public boolean typeMatches(Object target) {
        return wanted != null && target != null && target.getClass() == wanted.getClass();
    }
}
