/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.function.Function;

public interface FunctionAnswer<A, R> extends Function<A, R>, Answer<R> {

    @Override
    default R answer(InvocationOnMock invocation) throws Throwable {
        return this.apply(invocation.getArgument(0));
    }
}
