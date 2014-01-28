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
        if (annotation.serializable()) {
            mockSettings.serializable();
        }

        // see @Mock answer default value

        if (!annotation.customAnswer().equals(Answer.class)) {
            Constructor<Answer<?>> constructor = (Constructor<Answer<?>>) annotation.customAnswer().getConstructors()[0];
            try {
                mockSettings.defaultAnswer(constructor.newInstance(new Object[0]));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            mockSettings.defaultAnswer(annotation.answer().get());
        }
        return Mockito.mock(field.getType(), mockSettings);
    }
}
