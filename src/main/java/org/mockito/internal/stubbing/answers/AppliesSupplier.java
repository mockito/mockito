/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import java.util.function.Supplier;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AppliesSupplier implements Answer<Object>, Serializable {
    private static final long serialVersionUID = -7877552016409403742L;

    private final Supplier<?> supp;

    public AppliesSupplier(Supplier<?> supp) {
        this.supp = supp;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return supp.get();
    }

}
