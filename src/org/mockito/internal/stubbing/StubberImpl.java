package org.mockito.internal.stubbing;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class StubberImpl implements StubberFoo {

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

    public StubberFoo doReturn(Object toBeReturned) {
        answers.add(new Returns(toBeReturned));
        return this;
    }

    public StubberFoo doThrow(Throwable toBeThrown) {
        answers.add(new ThrowsException(toBeThrown));
        return this;
    }

    public StubberFoo doReturn() {
        answers.add(new Returns());
        return this;
    }

    public StubberFoo doAnswer(Answer answer) {
        answers.add(answer);
        return this;
    }
}