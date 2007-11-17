/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class NotNull implements IArgumentMatcher {

    public static final NotNull NOT_NULL = new NotNull();
    
    private NotNull() {
        
    }
    
    public boolean matches(Object actual) {
        return actual != null;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("notNull()");
    }
}
