/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.Serializable;

import org.mockito.ReturnValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

//It's ok to suppress deprecation because this class goes away as soon as ReturnValues disappears in future release
@SuppressWarnings("deprecation")
public class AnswerReturnValuesAdapter implements Answer<Object>, Serializable {

    private static final long serialVersionUID = 1418158596876713469L;
    private final ReturnValues returnValues;

    public AnswerReturnValuesAdapter(ReturnValues returnValues) {
        this.returnValues = returnValues;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        return returnValues.valueFor(invocation);
    }
}