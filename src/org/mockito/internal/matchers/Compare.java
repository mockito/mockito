/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.util.Comparator;


public class Compare<T> implements IArgumentMatcher {

    private T wanted;

    private Comparator<T> comparator;

    private LogicalOperator operator;

    public Compare(T wanted, Comparator<T> comparator, LogicalOperator result) {
        this.wanted = wanted;
        this.comparator = comparator;
        this.operator = result;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append(comparator + "(" + wanted + ") " + operator.getSymbol()
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
