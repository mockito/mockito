/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.MockitoMatcher;
import org.mockito.internal.matchers.text.ValuePrinter;

import java.io.Serializable;

public class Same extends MockitoMatcher<Object> implements Serializable {

    private final Object wanted;

    public Same(Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(Object actual) {
        return wanted == actual;
    }

    public String describe() {
        //TODO SF we should consider passing some kind of Formatter to the describe() method
        return "same(" + new ValuePrinter().appendValue(wanted).toString() + ")";
    }
}
