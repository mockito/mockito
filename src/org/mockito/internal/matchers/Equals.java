/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class Equals implements ArgumentMatcher<Object> {

    private final Object wanted;

    public Equals(Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(Object actual) {
        if (this.wanted == null) {
            return actual == null;
        }
        return wanted.equals(actual);
    }

    public void appendTo(StringBuilder buffer) {
        appendQuoting(buffer);
        buffer.append(wanted);
        appendQuoting(buffer);
    }

    private void appendQuoting(StringBuilder buffer) {
        if (wanted instanceof String) {
            buffer.append("\"");
        } else if (wanted instanceof Character) {
            buffer.append("'");
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
        return this.wanted == null && other.wanted == null
                || this.wanted != null
                && this.wanted.equals(other.wanted);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported");
    }
}