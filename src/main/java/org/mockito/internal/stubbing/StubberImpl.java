/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.stubbing.answers.*;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class StubberImpl implements Stubber {

    final List<Answer> answers = new LinkedList<Answer>();
    private final Reporter reporter = new Reporter();

    public <T> T when(T mock) {
        MockUtil mockUtil = new MockUtil();
        
        if (mock == null) {
            reporter.nullPassedToWhenMethod();
        } else {
            if (!mockUtil.isMock(mock)) {
                reporter.notAMockPassedToWhenMethod();
            }
        }
        
        mockUtil.getMockHandler(mock).setAnswersForStubbing(answers);
        return mock;
    }

    public Stubber doReturn(Object toBeReturned) {
        return doReturnValues(toBeReturned);
    }

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

    public Stubber doNothing() {
        answers.add(new DoesNothing());
        return this;
    }

    public Stubber doAnswer(Answer answer) {
        answers.add(answer);
        return this;
    }

    public Stubber doCallRealMethod() {
        answers.add(new CallsRealMethods());
        return this;
    }
}
