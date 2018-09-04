/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import java.util.function.Function;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AppliesFunction<T, R> implements Answer<Object>, Serializable {
    private static final long serialVersionUID = 4105249304302118590L;

    private final Function<T, R> func;

    public AppliesFunction(Function<T, R> func) {
        this.func = func;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        T t = invocation.getArgument(0);
        return func.apply(t);
    }

}
