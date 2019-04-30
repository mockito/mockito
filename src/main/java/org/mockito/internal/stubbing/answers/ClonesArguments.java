/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.lang.reflect.Array;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.creation.instance.Instantiator;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

//TODO this needs documentation and further analysis - what if someone changes the answer?
//we might think about implementing it straight on MockSettings
public class ClonesArguments implements Answer<Object> {
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            Object from = arguments[i];
            if (from != null) {
                if (from.getClass().isArray()) {
                    int len = Array.getLength(from);
                    Object newInstance = Array.newInstance(from.getClass().getComponentType(), len);
                    for (int j=0; j<len; ++j) {
                        Array.set(newInstance, j, Array.get(from, j));
                    }
                    arguments[i] = newInstance;
                } else {
                    Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(null);
                    Object newInstance = instantiator.newInstance(from.getClass());
                    new LenientCopyTool().copyToRealObject(from, newInstance);
                    arguments[i] = newInstance;
                }
            }
        }
        return new ReturnsEmptyValues().answer(invocation);
    }
}
