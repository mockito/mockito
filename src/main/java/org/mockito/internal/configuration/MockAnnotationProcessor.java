/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;

import java.lang.reflect.Field;

/**
 * Instantiates a mock on a field annotated by {@link Mock}
 */
public class MockAnnotationProcessor implements FieldAnnotationProcessor<Mock> {
    @Override
    public Object process(Mock annotation, Field field) {
        return processAnnotationForMock(annotation, field.getType(), field.getName());
    }

    public static Object processAnnotationForMock(Mock annotation, Class<?> type, String name) {
        MockSettings mockSettings = Mockito.withSettings();
        if (annotation.extraInterfaces().length > 0) { // never null
            mockSettings.extraInterfaces(annotation.extraInterfaces());
        }
        if ("".equals(annotation.name())) {
            mockSettings.name(name);
        } else {
            mockSettings.name(annotation.name());
        }
        if(annotation.serializable()){
            mockSettings.serializable();
        }
        if(annotation.stubOnly()){
            mockSettings.stubOnly();
        }
        if(annotation.lenient()){
            mockSettings.lenient();
        }

        // see @Mock answer default value
        mockSettings.defaultAnswer(annotation.answer());
        return Mockito.mock(type, mockSettings);
    }
}
