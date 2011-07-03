/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;


public class NotNull extends ArgumentMatcher<Object> implements Serializable {

    private static final long serialVersionUID = 7278261081285153228L;
    public static final NotNull NOT_NULL = new NotNull();
    
    private NotNull() {
        
    }
    
    public boolean matches(Object actual) {
        return actual != null;
    }

    public void describeTo(Description description) {
        description.appendText("notNull()");
    }
}
