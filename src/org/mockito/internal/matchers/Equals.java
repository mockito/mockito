/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.MockitoMatcher;

import java.io.Serializable;

public class Equals extends MockitoMatcher<Object> implements ContainsTypedDescription, Serializable {

    private final Object wanted;

    public Equals(Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(Object actual) {
        return Equality.areEqual(this.wanted, actual);
    }

    public String describe() {
        return describe(wanted);
    }

    public String describe(Object object) {
        return quoting() + object + quoting();
    }

    private String quoting() {
        if (wanted instanceof String) {
            return "\"";
        } else if (wanted instanceof Character) {
            return "'";
        } else {
            return "";
        }
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

    public String getTypedDescription() {
        return "("+ wanted.getClass().getSimpleName() +") " + describe(wanted);
    }

    public boolean typeMatches(Object object) {
        return wanted != null && object != null && object.getClass() == wanted.getClass();
    }
}