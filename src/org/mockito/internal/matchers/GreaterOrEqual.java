/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

public class GreaterOrEqual<T extends Comparable<T>> extends CompareTo<T> {

    private static final long serialVersionUID = 87695769061286092L;

    public GreaterOrEqual(Comparable<T> value) {
        super(value);
    }

    @Override
    protected String getName() {
        return "geq";
    }

    @Override
    protected boolean matchResult(int result) {
        return result >= 0;
    }
}
