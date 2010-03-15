/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.List;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

@SuppressWarnings("unchecked")
public interface MockHandlerInterface<T> {

    MockSettingsImpl getMockSettings();

    VoidMethodStubbable<T> voidMethodStubbable(T mock);
    
    void setAnswersForStubbing(List<Answer> answers);

    InvocationContainer getInvocationContainer();
}