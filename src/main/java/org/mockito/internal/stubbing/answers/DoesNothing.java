/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

import static org.mockito.internal.exceptions.Reporter.onlyVoidMethodsCanBeSetToDoNothing;

public class DoesNothing implements Answer<Object>, ValidableAnswer, Serializable {
    
    private static final long serialVersionUID = 4840880517740698416L;

    public Object answer(InvocationOnMock invocation) throws Throwable {
        return null;
    }

    @Override
    public void validateFor(InvocationOnMock invocation) {
        if (!new InvocationInfo(invocation).isVoid()) {
            throw onlyVoidMethodsCanBeSetToDoNothing();
        }
    }
}
