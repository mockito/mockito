/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

@SuppressWarnings("unchecked")
public class Any extends ArgumentMatcher implements Serializable {

    private static final long serialVersionUID = -4062420125651019029L;
    public static final Any ANY = new Any();    
    
    private Any() {}
    
    public boolean matches(Object actual) {
        return true;
    }

    public void describeTo(Description description) {
        description.appendText("<any>");
    }
}
