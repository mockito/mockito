/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;

/**
 * Author: Szczepan Faber
 */
public class InvocationContainerImplTest {

    InvocationContainerImpl container = new InvocationContainerImpl(new MockSettingsImpl());
    InvocationContainerImpl containerStubOnly =
            new InvocationContainerImpl((MockCreationSettings) new MockSettingsImpl().stubOnly());
    Invocation invocation = new InvocationBuilder().toInvocation();
    LinkedList<Throwable> exceptions = new LinkedList<Throwable>();

    @Test
    public void should_be_thread_safe() throws Throwable {
        doShouldBeThreadSafe(container);
    }

    @Test
    public void should_be_thread_safe_stub_only() throws Throwable {
        doShouldBeThreadSafe(containerStubOnly);
    }

    // works 50% of the time
    private void doShouldBeThreadSafe(final InvocationContainerImpl c) throws Throwable {
        // given
        Thread[] t = new Thread[200];
        final CountDownLatch starter = new CountDownLatch(200);
        for (int i = 0; i < t.length; i++) {
            t[i] =
                    new Thread() {
                        public void run() {
                            try {
                                starter.await(); // NOPMD
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            c.setInvocationForPotentialStubbing(new InvocationMatcher(invocation));
                            c.addAnswer(new Returns("foo"), null);
                            c.findAnswerFor(invocation);
                        }
                    };
            t[i].setUncaughtExceptionHandler(
                    new Thread.UncaughtExceptionHandler() {
                        public void uncaughtException(Thread t, Throwable e) {
                            exceptions.add(e);
                        }
                    });
            t[i].start();

            starter.countDown();
        }

        // when
        for (Thread aT : t) {
            aT.join();
        }

        // then
        if (exceptions.size() != 0) {
            throw exceptions.getFirst();
        }
    }

    @Test
    public void should_return_invoked_mock() throws Exception {
        container.setInvocationForPotentialStubbing(new InvocationMatcher(invocation));

        assertEquals(invocation.getMock(), container.invokedMock());
    }

    @Test
    public void should_return_invoked_mock_stub_only() throws Exception {
        containerStubOnly.setInvocationForPotentialStubbing(new InvocationMatcher(invocation));

        assertEquals(invocation.getMock(), containerStubOnly.invokedMock());
    }

    @Test
    public void should_tell_if_has_invocation_for_potential_stubbing() throws Exception {
        container.setInvocationForPotentialStubbing(new InvocationBuilder().toInvocationMatcher());
        assertTrue(container.hasInvocationForPotentialStubbing());

        container.addAnswer(new ReturnsEmptyValues(), null);
        assertFalse(container.hasInvocationForPotentialStubbing());
    }

    @Test
    public void should_tell_if_has_invocation_for_potential_stubbing_stub_only() throws Exception {
        containerStubOnly.setInvocationForPotentialStubbing(
                new InvocationBuilder().toInvocationMatcher());
        assertTrue(containerStubOnly.hasInvocationForPotentialStubbing());

        containerStubOnly.addAnswer(new ReturnsEmptyValues(), null);
        assertFalse(containerStubOnly.hasInvocationForPotentialStubbing());
    }

    @Test
    public void should_return_answer_when_answer_exists() throws Throwable {
        // Given: set an invocation for stubbing and add a matching answer
        container.setInvocationForPotentialStubbing(new InvocationMatcher(invocation));
        container.addAnswer(new Returns("Expected Answer"), null);

        // When: invoking answerTo should return the answer
        Object result = container.answerTo(invocation);

        // Then: verify the expected answer is returned
        assertEquals("Expected Answer", result);
    }

    @Test
    public void should_throw_mockito_exception_when_no_answer_found() {
        // Given: no answer added for the invocation
        Invocation unmatchedInvocation = new InvocationBuilder().toInvocation();

        // When and Then: invoking answerTo should throw MockitoException
        MockitoException exception =
                assertThrows(
                        MockitoException.class,
                        () -> {
                            container.answerTo(unmatchedInvocation);
                        });

        // Verify the exception message
        assertTrue(
                exception
                        .getMessage()
                        .contains(
                                "Unable to find answer for: "
                                        + unmatchedInvocation
                                        + "Did you configure a stubbing for this method?"));
    }
}
