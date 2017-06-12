/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Returns as the provided answer would return, after delaying the specified amount.
 *
 * <p>The <code>sleepyTime</code> specifies how long, in milliseconds, to pause before
 * returning the provided <code>answer</code>.</p>
 *
 * @since 2.8.44
 * @see org.mockito.AdditionalAnswers
 */
public class AnswersWithDelay implements Answer<Object>, ValidableAnswer, Serializable {
    private static final long serialVersionUID = 2177950597971260246L;

    private final long sleepyTime;
    private final Answer<Object> answer;

    public AnswersWithDelay(final long sleepyTime, final Answer<Object> answer) {
        this.sleepyTime = sleepyTime;
        this.answer = answer;
    }

    @Override
    public Object answer(final InvocationOnMock invocation) throws Throwable {
        TimeUnit.MILLISECONDS.sleep(sleepyTime);
        return answer.answer(invocation);
    }

    @Override
    public void validateFor(final InvocationOnMock invocation) {
        if (answer instanceof ValidableAnswer) {
            ((ValidableAnswer) answer).validateFor(invocation);
        }
    }
}

