/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public interface Stubber extends StubbedMethodSelector {

    Stubber doReturn(Object toBeReturned);
    Stubber doReturn();
    Stubber doThrow(Throwable toBeThrown);
    Stubber doAnswer(Answer answer);
    
}