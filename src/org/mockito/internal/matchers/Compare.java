/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.util.Comparator;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

@SuppressWarnings("unchecked")
public class Compare<T> extends ArgumentMatcher {

    private T wanted;

    private Comparator<T> comparator;

    private LogicalOperator operator;

    public Compare(T wanted, Comparator<T> comparator, LogicalOperator result) {
        this.wanted = wanted;
        this.comparator = comparator;
        this.operator = result;
    }

    public void describeTo(Description description) {
        description.appendText(comparator + "(" + wanted + ") " + operator.getSymbol()
                + " 0");
    }

    @SuppressWarnings("unchecked")
    public boolean matches(Object actual) {
        if(actual == null) {
            return false;
        }
        return operator.matchResult(comparator.compare((T) actual, wanted));
    }

}
