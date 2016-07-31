/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.mockito.internal.exceptions.Reporter.notAMockPassedToWhenMethod;
import static org.mockito.internal.exceptions.Reporter.nullPassedToWhenMethod;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.ThrowsExceptionClass;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

@SuppressWarnings("unchecked")
public class StubberImpl implements Stubber {

    private final List<Answer<?>> answers = new LinkedList<Answer<?>>();

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

    public Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return doThrowClasses(toBeThrown);
    }

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
