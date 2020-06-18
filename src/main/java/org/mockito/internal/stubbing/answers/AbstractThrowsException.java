/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.mockito.internal.exceptions.Reporter.cannotStubWithNullThrowable;
import static org.mockito.internal.exceptions.Reporter.checkedExceptionInvalid;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;

public abstract class AbstractThrowsException implements Answer<Object>, ValidableAnswer {

    private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

    protected abstract Throwable getThrowable();

    public Object answer(InvocationOnMock invocation) throws Throwable {
        Throwable throwable = getThrowable();
        if (throwable == null) {
            throw new IllegalStateException(
                    "throwable is null: " + "you shall not call #answer if #validateFor fails!");
        }
        if (MockUtil.isMock(throwable)) {
            throw throwable;
        }
        Throwable t = throwable.fillInStackTrace();

        if (t == null) {
            // Custom exceptions sometimes return null, see #866
            throw throwable;
        }
        filter.filter(t);
        throw t;
    }

    @Override
    public void validateFor(InvocationOnMock invocation) {
        Throwable throwable = getThrowable();
        if (throwable == null) {
            throw cannotStubWithNullThrowable();
        }

        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }

        if (!new InvocationInfo(invocation).isValidException(throwable)) {
            throw checkedExceptionInvalid(throwable);
        }
    }
}
