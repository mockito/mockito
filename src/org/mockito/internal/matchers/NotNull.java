/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class NotNull implements IArgumentMatcher<Object> {

    public static final NotNull NOT_NULL = new NotNull();
    
    private NotNull() {
        
    }
    
    public boolean matches(Object actual) {
        return actual != null;
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("notNull()");
    }
}
