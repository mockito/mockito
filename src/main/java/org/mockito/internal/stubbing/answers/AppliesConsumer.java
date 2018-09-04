/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import java.util.function.Consumer;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AppliesConsumer<T> implements Answer<Object>, Serializable {
    private static final long serialVersionUID = 3515432710365798383L;

    private final Consumer<T> cons;

    public AppliesConsumer(Consumer<T> cons) {
        this.cons = cons;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        T t = invocation.getArgument(0);
        cons.accept(t);
        return null;
    }

}
