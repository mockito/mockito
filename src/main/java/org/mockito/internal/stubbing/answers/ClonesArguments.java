/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.objenesis.ObjenesisHelper;

//TODO this needs documentation and further analysis - what if someone changes the answer?
//we might think about implementing it straight on MockSettings
public class ClonesArguments implements Answer<Object> {
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            Object from = arguments[i];
            Object newInstance = ObjenesisHelper.newInstance(from.getClass());
            new LenientCopyTool().copyToRealObject(from, newInstance);
            arguments[i] = newInstance;
        }
        return new ReturnsEmptyValues().answer(invocation);
    }
}