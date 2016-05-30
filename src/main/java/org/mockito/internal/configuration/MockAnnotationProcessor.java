/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;

/**
 * Instantiates a mock on a field annotated by {@link Mock}
 */
public class MockAnnotationProcessor implements FieldAnnotationProcessor<Mock> {
    public Object process(Mock annotation, Field field) {
        MockSettings mockSettings = Mockito.withSettings();
        if (annotation.extraInterfaces().length > 0) { // never null
            mockSettings.extraInterfaces(annotation.extraInterfaces());
        }
        if ("".equals(annotation.name())) {
            mockSettings.name(field.getName());
        } else {
            mockSettings.name(annotation.name());
        }
        if(annotation.serializable()){
            mockSettings.serializable();
        }

        // see @Mock answer default value
        mockSettings.defaultAnswer(getDefaultAnswer(annotation.answerClass(), annotation.answer(), field));
        return mock(field.getType(), mockSettings);
    }

    private Answer<?> getDefaultAnswer(Class<? extends Answer> answerClass, Answers defaultAnswer, Field field) {
        if (hasAnswerClassDefined(answerClass)) {
            validateAnswerClassUse(defaultAnswer, field);
            return instantiateAnswerClass(answerClass);
        }
        return defaultAnswer.get();
    }

    private Answer<?> instantiateAnswerClass(Class<? extends Answer> answerClass) {
        if (answerClass.getConstructors().length == 0) {
            throw new MockitoException("Custom answer cannot be instantiated",null);
        }
        try {
            Constructor<?> constructor = answerClass.getConstructor();
            return (Answer<?>) constructor.newInstance();
        } catch (Exception e) {
            throw new MockitoException("Custom answer cannot be instantiated",e);
        }
    }

    private void validateAnswerClassUse(Answers defaultAnswer, Field field) {
        if (defaultAnswer != Answers.RETURNS_DEFAULTS) {
            new Reporter().answerAndCustomAnswerInMockAnnotationNotAllowed(field.getName());
        }
    }

    private boolean hasAnswerClassDefined(Class<? extends Answer> answerClass) {
        return !answerClass.equals(Answer.class);
    }
}
