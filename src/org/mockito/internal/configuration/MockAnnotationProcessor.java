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
import org.mockito.stubbing.Answer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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
        if (annotation.serializable()) {
            mockSettings.serializable();
        }

        mockSettings.defaultAnswer(getDefaultAnswer(annotation.customAnswer(), annotation.answer(), field));
        return Mockito.mock(field.getType(), mockSettings);
    }

    private Answer<?> getDefaultAnswer(Class<? extends Answer> customAnswer, Answers defaultAnswer, Field field) {

        if (hasCustomAnswerDefined(customAnswer)) {
            reportCustomAnswerMisuse(defaultAnswer, field);

            return instantiateCustomAnswer(customAnswer);
        }
        return defaultAnswer.get();
    }

    private Answer<?> instantiateCustomAnswer(Class<? extends Answer> customAnswer) {
        if(customAnswer.getConstructors().length == 0) {
            throw new Reporter().reportCustomAnswerCannotBeInstantiated(null);
        }
        try {
            Constructor<?> constructor = customAnswer.getConstructor();
            return (Answer<?>) constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw new Reporter().reportCustomAnswerCannotBeInstantiated(e);
        } catch (InstantiationException e) {
            throw new Reporter().reportCustomAnswerCannotBeInstantiated(e);
        } catch (IllegalAccessException e) {
            throw new Reporter().reportCustomAnswerCannotBeInstantiated(e);
        } catch (NoSuchMethodException e) {
            throw new Reporter().reportCustomAnswerCannotBeInstantiated(e);
        }
    }

    private void reportCustomAnswerMisuse(Answers defaultAnswer, Field field) {
        if (defaultAnswer != Answers.RETURNS_DEFAULTS) {
            new Reporter().answerAndCustomAnswerInMockAnnotationNotAllowed(field.getName());
        }
    }

    private boolean hasCustomAnswerDefined(Class<? extends Answer> customAnswer) {
        return !customAnswer.equals(Answer.class);
    }

}
