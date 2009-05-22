/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Globally configured Answer.
 * <p>
 * See javadoc for {@link IMockitoConfiguration}
 */
public class GloballyConfiguredAnswer implements Answer<Object> {
    
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return new GlobalConfiguration().getDefaultAnswer().answer(invocation);
    }
}
