package org.mockito.internal.stubbing;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;

public class MethodSelectorImpl implements MethodSelector {

    private final Answer<?> answer;
    private final Reporter reporter = new Reporter();

    public MethodSelectorImpl(Answer<?> answer) {
        this.answer = answer;
    }

    public <T> T when(T mock) {
        if (mock == null) {
            reporter.nullPassedToWhenMethod();
        } else if (!MockUtil.isMock(mock)) {
            reporter.notAMockPassedToWhenMethod();
        }
        
        MockUtil.getMockHandler(mock).setAnswerForStubbing(answer);
        return mock;
    }
}