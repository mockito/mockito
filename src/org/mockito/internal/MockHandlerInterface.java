package org.mockito.internal;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.List;

public interface MockHandlerInterface<T> {

    MockSettingsImpl getMockSettings();

    void verifyNoMoreInteractions();

    VoidMethodStubbable<T> voidMethodStubbable(T mock);

    void setAnswersForStubbing(List<Answer> answers);

    InvocationContainer getInvocationContainer();
}