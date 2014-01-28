/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldInitializer;
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
        if(annotation.serializable()){
            mockSettings.serializable();
        }

        mockSettings.defaultAnswer(getDefaultAnswer(annotation));
        return Mockito.mock(field.getType(), mockSettings);
    }

    private Answer<?> getDefaultAnswer(Mock annotation) {
        if (hasCustomAnswerDefined(annotation)) {
            try {
                return annotation.customAnswer().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return annotation.answer().get();
    }

    private boolean hasCustomAnswerDefined(Mock annotation) {
        return !annotation.customAnswer().equals(Answer.class);
    }
}
