/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public interface StubberFoo extends StubbedMethodSelector {

    StubberFoo doReturn(Object toBeReturned);
    StubberFoo doReturn();
    StubberFoo doThrow(Throwable toBeThrown);
    StubberFoo doAnswer(Answer answer);
    
}