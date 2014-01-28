/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;

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

        mockSettings.defaultAnswer(getDefaultAnswer(annotation.customAnswer(), annotation.answer()));
        return Mockito.mock(field.getType(), mockSettings);
    }

    private Answer<?> getDefaultAnswer(Class<? extends Answer> customAnswer, Answers defaultAnswer) {

        if (hasCustomAnswerDefined(customAnswer)) {
            throwExceptionIfAnswerAndCustomAnswerDefined(defaultAnswer);

            return instantiateCustomAnswer(customAnswer);
        }
        return defaultAnswer.get();
    }

    private Answer<?> instantiateCustomAnswer(Class<? extends Answer> customAnswer) {
        try {
            return customAnswer.newInstance();
        } catch (Exception e) {
            throw new MockitoException("Could not process customAnswer", e);
        }
    }

    private void throwExceptionIfAnswerAndCustomAnswerDefined(Answers defaultAnswer) {
        if(defaultAnswer.isReturnDefaultsAnswer()){
            throw new MockitoException("You cannot define answer and customAnswer at the same time");
        }
    }

    private boolean hasCustomAnswerDefined(Class<? extends Answer> customAnswer) {
        return !customAnswer.equals(Answer.class);
    }

}
