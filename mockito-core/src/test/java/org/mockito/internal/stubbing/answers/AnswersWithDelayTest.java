/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.util.Date;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationBuilder;

public class AnswersWithDelayTest {
    @Test
    public void should_return_value() throws Throwable {
        assertThat(
                        new AnswersWithDelay(1, new Returns("value"))
                                .answer(
                                        new InvocationBuilder()
                                                .method("oneArg")
                                                .arg("A")
                                                .toInvocation()))
                .isEqualTo("value");
    }

    @Test
    public void should_fail_when_contained_answer_should_fail() {
        assertThatThrownBy(
                        () -> {
                            new AnswersWithDelay(1, new Returns("one"))
                                    .validateFor(
                                            new InvocationBuilder()
                                                    .method("voidMethod")
                                                    .toInvocation());
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "'voidMethod' is a *void method* and it *cannot* be stubbed with a *return value*!",
                        "Voids are usually stubbed with Throwables:",
                        "    doThrow(exception).when(mock).someVoidMethod();",
                        "If you need to set the void method to do nothing you can use:",
                        "    doNothing().when(mock).someVoidMethod();",
                        "For more information, check out the javadocs for Mockito.doNothing().");
    }

    @Test
    public void should_succeed_when_contained_answer_should_succeed() {
        new AnswersWithDelay(1, new Returns("one"))
                .validateFor(new InvocationBuilder().simpleMethod().toInvocation());
    }

    @Test
    public void should_delay() throws Throwable {
        final long sleepyTime = 500L;

        final AnswersWithDelay testSubject = new AnswersWithDelay(sleepyTime, new Returns("value"));

        final Date before = new Date();
        testSubject.answer(new InvocationBuilder().method("oneArg").arg("A").toInvocation());
        final Date after = new Date();

        final long timePassed = after.getTime() - before.getTime();
        assertThat(timePassed).isCloseTo(sleepyTime, within(15L));
    }
}
