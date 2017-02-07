/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.mockito.internal.exceptions.Reporter.notAMockPassedToWhenMethod;
import static org.mockito.internal.exceptions.Reporter.nullPassedToWhenMethod;
import static org.mockito.internal.stubbing.answers.DoesNothing.doesNothing;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.ThrowsExceptionClass;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

@SuppressWarnings("unchecked")
public class StubberImpl implements Stubber {

    private final List<Answer<?>> answers = new LinkedList<Answer<?>>();

    @Override
    public <T> T when(T mock) {
        if (mock == null) {
            throw nullPassedToWhenMethod();
        }

		if (!MockUtil.isMock(mock)) {
			throw notAMockPassedToWhenMethod();
		}

		MockUtil.getMockHandler(mock).setAnswersForStubbing(answers);
        return mock;
    }

    @Override
    public Stubber doReturn(Object toBeReturned) {
        return doReturnValues(toBeReturned);
    }

    @Override
    public Stubber doReturn(Object toBeReturned, Object... nextToBeReturned) {
        return doReturnValues(toBeReturned).doReturnValues(nextToBeReturned);
    }

    private StubberImpl doReturnValues(Object... toBeReturned) {
        if(toBeReturned == null) {
            answers.add(new Returns(null));
            return this;
        }
        for (Object r : toBeReturned) {
            answers.add(new Returns(r));
        }
        return this;
    }

    @Override
    public Stubber doThrow(Throwable... toBeThrown) {
        if(toBeThrown == null) {
            answers.add(new ThrowsException(null));
            return this;
        }
        for (Throwable throwable : toBeThrown) {
            answers.add(new ThrowsException(throwable));
        }
        return this;
    }

    @Override
    public Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return doThrowClasses(toBeThrown);
    }

    @Override
    public Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        return doThrowClasses(toBeThrown).doThrowClasses(nextToBeThrown);
    }

    private StubberImpl doThrowClasses(Class<? extends Throwable>... toBeThrown) {
        for (Class<? extends Throwable> throwable: toBeThrown) {
            answers.add(new ThrowsExceptionClass(throwable));
        }
        return this;
    }

    @Override
    public Stubber doNothing() {
        answers.add(doesNothing());
        return this;
    }

    @Override
    public Stubber doAnswer(Answer answer) {
        answers.add(answer);
        return this;
    }

    @Override
    public Stubber doCallRealMethod() {
        answers.add(new CallsRealMethods());
        return this;
    }
}
