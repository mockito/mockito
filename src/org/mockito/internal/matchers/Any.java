/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

@SuppressWarnings("unchecked")
public class Any implements ArgumentMatcher {

    public static final Any ANY = new Any();    
    
    private Any() {}
    
    public boolean matches(Object actual) {
        return true;
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("<any>");
    }
}
