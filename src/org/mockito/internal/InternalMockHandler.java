/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.List;

import org.mockito.Incubating;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

@SuppressWarnings("unchecked")
public interface InternalMockHandler<T> extends MockHandler {

    MockCreationSettings getMockSettings();

    VoidMethodStubbable<T> voidMethodStubbable(T mock);
    
    void setAnswersForStubbing(List<Answer> answers);

    InvocationContainer getInvocationContainer();

    /**
     * Takes an invocation object and handles it.
     * <p>
     * The default implementation provided by Mockito handles invocations by recording
     * method calls on mocks for further verification, captures the stubbing information when mock is stubbed,
     * returns the stubbed values for invocations that have been stubbed, and much more.
     *
     * @param invocation The invocation to handle
     * @return Result
     * @throws Throwable Throwable
     */
    @Incubating
    Object handle(Invocation invocation) throws Throwable;
}