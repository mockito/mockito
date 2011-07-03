/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

public class LessOrEqual<T extends Comparable<T>> extends CompareTo<T> implements Serializable {

    private static final long serialVersionUID = -6648773374429103565L;

    public LessOrEqual(Comparable<T> value) {
        super(value);
    }

    @Override
    protected String getName() {
        return "leq";
    }

    @Override
    protected boolean matchResult(int result) {
        return result <= 0;
    }
}
