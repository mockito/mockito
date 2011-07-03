/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

public class GreaterThan<T extends Comparable<T>> extends CompareTo<T> implements Serializable {

    private static final long serialVersionUID = 7446529803235604408L;

    public GreaterThan(Comparable<T> value) {
        super(value);
    }

    @Override
    protected String getName() {
        return "gt";
    }

    @Override
    protected boolean matchResult(int result) {
        return result > 0;
    }    
}
