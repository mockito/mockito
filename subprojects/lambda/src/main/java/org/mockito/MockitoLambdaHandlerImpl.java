package org.mockito;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class MockitoLambdaHandlerImpl<T> implements MockHandler<T> {

    private final MockCreationSettings<T> settings;

    private final MatchersBinder matchersBinder;

    private final InvocationContainerImpl invocationContainer;

    static Answer<?> answerValue;

    MockitoLambdaHandlerImpl(MockCreationSettings<T> settings) {
        this.settings = settings;
        this.matchersBinder = new MatchersBinder();
        this.invocationContainer = new InvocationContainerImpl(settings);
    }

    @Override
    public Object handle(Invocation invocation) throws Throwable {
        if (answerValue != null) {
            this.saveStubbing(invocation);
        } else {
            // It's a regular invocation. Try to see if we have a stub for it
            StubbedInvocationMatcher answer = invocationContainer.findAnswerFor(invocation);

            if (answer != null) {
                return answer.answer(invocation);
            }
        }

        // At this point the method was not stubbed. Make sure to return primitive values, to prevent
        // NPE due to Java auto-unboxing
        Class<?> type = invocation.getMethod().getReturnType();

        if (Primitives.isPrimitiveOrWrapper(type)) {
            return Primitives.defaultValue(type);
        }

        return null;
    }

    private void saveStubbing(Invocation invocation) {
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(
            mockingProgress().getArgumentMatcherStorage(),
            invocation
        );

        invocationContainer.setAnswersForStubbing(Collections.singletonList(answerValue));
        invocationContainer.setMethodForStubbing(invocationMatcher);

        answerValue = null;
    }

    @Override
    public MockCreationSettings<T> getMockSettings() {
        return settings;
    }

    @Override
    public InvocationContainer getInvocationContainer() {
        return invocationContainer;
    }
}
