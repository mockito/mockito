/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.ReturnValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

//It's ok to suppress deprecation because this class goes away as soon as ReturnValues disappears in future release
@SuppressWarnings("deprecation")
public class AnswerReturnValuesAdapter implements Answer<Object> {

    private final ReturnValues returnValues;

    public AnswerReturnValuesAdapter(ReturnValues returnValues) {
        this.returnValues = returnValues;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        return returnValues.valueFor(invocation);
    }
}