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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class StubberImpl implements Stubber {

    private static final TransformToAnswerCommand<Object> transformObjectToAnswerCommand = new TransformToAnswerCommand<Object>() {
        @Override
        public Answer toAnswer(Object o) {
            return new Returns(o);
        }
    };

    private static final TransformToAnswerCommand<Throwable> transformThrowableToAnswerCommand = new TransformToAnswerCommand<Throwable>() {
        @Override
        public Answer toAnswer(Throwable t) {
            return new ThrowsException(t);
        }
    };

    private static final TransformToAnswerCommand<Class<? extends Throwable>> transformThrowableClassToAnswerCommand = new TransformToAnswerCommand<Class<? extends Throwable>>() {
        @Override
        public Answer toAnswer(Class<? extends Throwable> c) {
            return new ThrowsExceptionClass(c);
        }
    };

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
        return handleDoMethods(transformObjectToAnswerCommand, toBeReturned);
    }

    public Stubber doThrow(Throwable toBeThrown){
        return handleDoMethods(transformThrowableToAnswerCommand, toBeThrown);
    }

    public Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return handleDoMethods(transformThrowableClassToAnswerCommand, toBeThrown);
    }

    public Stubber doReturn(Object toBeReturned, Object... nextToBeReturned) {
        return handleDoMethods(transformObjectToAnswerCommand, toBeReturned, nextToBeReturned);
    }

    public Stubber doThrow(Throwable toBeThrown, Throwable... nextToBeThrown){
        return handleDoMethods(transformThrowableToAnswerCommand, toBeThrown, nextToBeThrown);
    }

    public Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        return handleDoMethods(transformThrowableClassToAnswerCommand, toBeThrown, nextToBeThrown);
    }

    public Stubber doNothing() {
        answers.add(new DoesNothing());
        return this;
    }

    public Stubber doAnswer(Answer answer) {
        answers.add(answer);
        return this;
    }

    public Stubber doAnswer(Answer answer, Answer... nextAnswers) {
        doAnswer(answer);
        Collections.addAll(answers,nextAnswers);
        return this;
    }

    public Stubber doCallRealMethod() {
        answers.add(new CallsRealMethods());
        return this;
    }

    private <T> Stubber handleDoMethods(TransformToAnswerCommand<T> transformCommand, T firstElement, T... elements){
        Answer firstAnswer =  transformCommand.toAnswer(firstElement);
        Answer[] answers = new Answer[elements.length];
        for (int index = 0; index < elements.length; ++index ){
            answers[index] = transformCommand.toAnswer(elements[index]);
        }
        return doAnswer(firstAnswer, answers);
    }

    private interface TransformToAnswerCommand<T>{
        Answer toAnswer(T t);
    }
}
