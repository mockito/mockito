/*
 * Copyright (c) 2001-2007 OFFIS, Henri Tremblay.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.util.Comparator;


public class Compare<T> implements IArgumentMatcher {

    private T expected;

    private Comparator<T> comparator;

    private LogicalOperator operator;

    public Compare(T expected, Comparator<T> comparator, LogicalOperator result) {
        this.expected = expected;
        this.comparator = comparator;
        this.operator = result;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append(comparator + "(" + expected + ") " + operator.getSymbol()
                + " 0");
    }

    @SuppressWarnings("unchecked")
    public boolean matches(Object actual) {
        if(actual == null) {
            return false;
        }
        return operator.matchResult(comparator.compare((T) actual, expected));
    }

}
