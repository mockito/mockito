/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.invocation.Invocation;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class InvocationContainerImplStubbingTest extends TestBase {

    private InvocationContainerImpl invocationContainerImpl;
    private InvocationContainerImpl invocationContainerImplStubOnly;
    private MockingProgress state;
    private Invocation simpleMethod;

    @Before
    public void setup() {
        state = mockingProgress();

        invocationContainerImpl = new InvocationContainerImpl(new MockSettingsImpl());
        invocationContainerImpl.setInvocationForPotentialStubbing(new InvocationBuilder().toInvocationMatcher());

        invocationContainerImplStubOnly =
          new InvocationContainerImpl( new MockSettingsImpl().stubOnly());
        invocationContainerImplStubOnly.setInvocationForPotentialStubbing(new InvocationBuilder().toInvocationMatcher());

        simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
    }

    @Test
    public void should_finish_stubbing_when_wrong_throwable_is_set() throws Exception {
        state.stubbingStarted();
        try {
            invocationContainerImpl.addAnswer(new ThrowsException(new Exception()), null);
            fail();
        } catch (MockitoException e) {
            state.validateState();
        }
    }

    @Test
    public void should_finish_stubbing_on_adding_return_value() throws Exception {
        state.stubbingStarted();
        invocationContainerImpl.addAnswer(new Returns("test"), null);
        state.validateState();
    }

    @Test
    public void should_get_results_for_methods() throws Throwable {
        invocationContainerImpl.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        invocationContainerImpl.addAnswer(new Returns("simpleMethod"), null);

        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        invocationContainerImpl.setInvocationForPotentialStubbing(new InvocationMatcher(differentMethod));
        invocationContainerImpl.addAnswer(new ThrowsException(new MyException()), null);

        assertEquals("simpleMethod", invocationContainerImpl.answerTo(simpleMethod));

        try {
            invocationContainerImpl.answerTo(differentMethod);
            fail();
        } catch (MyException e) {}
    }

    @Test
    public void should_get_results_for_methods_stub_only() throws Throwable {
        invocationContainerImplStubOnly.setInvocationForPotentialStubbing(new InvocationMatcher(simpleMethod));
        invocationContainerImplStubOnly.addAnswer(new Returns("simpleMethod"), null);

        Invocation differentMethod = new InvocationBuilder().differentMethod().toInvocation();
        invocationContainerImplStubOnly.setInvocationForPotentialStubbing(new InvocationMatcher(differentMethod));
        invocationContainerImplStubOnly.addAnswer(new ThrowsException(new MyException()), null);

        assertEquals("simpleMethod", invocationContainerImplStubOnly.answerTo(simpleMethod));

        try {
            invocationContainerImplStubOnly.answerTo(differentMethod);
            fail();
        } catch (MyException e) {}
    }

    @Test
    public void should_validate_throwable() throws Throwable {
        try {
            invocationContainerImpl.addAnswer(new ThrowsException(null), null);
            fail();
        } catch (MockitoException e) {}
    }

    @SuppressWarnings("serial")
    class MyException extends RuntimeException {}
}
