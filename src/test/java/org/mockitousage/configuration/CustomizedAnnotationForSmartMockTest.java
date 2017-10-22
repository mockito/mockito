/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.InjectingAnnotationEngine;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static java.lang.annotation.ElementType.FIELD;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;


/**
 * @see MockitoConfiguration#getAnnotationEngine() for the custom smartmock injection engine
 */
public class CustomizedAnnotationForSmartMockTest extends TestBase {

    @SmartMock IMethods smartMock;

    @Test
    public void shouldUseCustomAnnotation() {
        assertEquals("SmartMock should return empty String by default", "", smartMock.simpleMethod(1));
        verify(smartMock).simpleMethod(1);
    }

    @Target({FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SmartMock {}

    public static class CustomInjectingAnnotationEngine extends InjectingAnnotationEngine {
        @Override
        protected void onInjection(Object testClassInstance, Class<?> clazz, Set<Field> mockDependentFields, Set<Object> mocks) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(SmartMock.class)) {
                    field.setAccessible(true);
                    try {
                        field.set(Modifier.isStatic(field.getModifiers()) ? null : testClassInstance, Mockito.mock(field.getType(), Mockito.RETURNS_SMART_NULLS));
                    } catch (Exception exception) {
                        throw new AssertionError(exception.getMessage());
                    }
                }
            }
        }
    }
}
