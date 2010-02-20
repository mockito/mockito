package org.mockito.internal;

import java.util.List;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

@SuppressWarnings("unchecked")
public interface MockHandlerInterface<T> {

    MockSettingsImpl getMockSettings();

    void verifyNoMoreInteractions();

    VoidMethodStubbable<T> voidMethodStubbable(T mock);
    
    void setAnswersForStubbing(List<Answer> answers);

    InvocationContainer getInvocationContainer();
}