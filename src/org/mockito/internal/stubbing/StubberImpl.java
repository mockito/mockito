/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class StubberImpl implements Stubber {

    final List<Answer> answers = new LinkedList<Answer>();
    private final Reporter reporter = new Reporter();

    public <T> T when(T mock) {
        if (mock == null) {
            reporter.nullPassedToWhenMethod();
        } else if (!MockUtil.isMock(mock)) {
            reporter.notAMockPassedToWhenMethod();
        }
        
        MockUtil.getMockHandler(mock).setAnswersForStubbing(answers);
        return mock;
    }

    public Stubber doReturn(Object toBeReturned) {
        answers.add(new Returns(toBeReturned));
        return this;
    }

    public Stubber doThrow(Throwable toBeThrown) {
        answers.add(new ThrowsException(toBeThrown));
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
}