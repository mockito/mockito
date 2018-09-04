/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import java.util.function.BiFunction;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AppliesBiFunction<T, U, R> implements Answer<Object>, Serializable {
    private static final long serialVersionUID = -2320854589863776136L;

    private final BiFunction<T, U, R> func;

    public AppliesBiFunction(BiFunction<T, U, R> func) {
        this.func = func;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        T t = invocation.getArgument(0);
        U u = invocation.getArgument(1);
        return func.apply(t, u);
    }

}
