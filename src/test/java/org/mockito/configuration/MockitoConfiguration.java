/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.Mockito;
import org.mockito.internal.configuration.InjectingAnnotationEngine;
import org.mockito.stubbing.Answer;
import org.mockitousage.configuration.SmartMock;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class MockitoConfiguration extends DefaultMockitoConfiguration implements IMockitoConfiguration {

    private Answer<Object> overriddenDefaultAnswer = null;

    private boolean cleansStackTrace;

    private AnnotationEngine overriddenEngine;

    private boolean enableClassCache = true;

    //for testing purposes, allow to override the configuration
    public void overrideDefaultAnswer(Answer<Object> defaultAnswer) {
        this.overriddenDefaultAnswer = defaultAnswer;
    }

    //for testing purposes, allow to override the configuration
    public void overrideCleansStackTrace(boolean cleansStackTrace) {
        this.cleansStackTrace = cleansStackTrace;
    }

    //for testing purposes, allow to override the annotation engine
    public void overrideAnnotationEngine(AnnotationEngine engine) {
        this.overriddenEngine = engine;
    }

    //for testing purposes, allow to override the annotation engine
    public void overrideEnableClassCache(boolean enableClassCache) {
        this.enableClassCache = enableClassCache;
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        if (overriddenDefaultAnswer == null) {
            return super.getDefaultAnswer();
        } else {
            return overriddenDefaultAnswer;
        }
    }

    @Override
    public AnnotationEngine getAnnotationEngine() {
        if (this.overriddenEngine != null) {
            return this.overriddenEngine;
        }
        return new InjectingAnnotationEngine() {
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
        };
    }

    @Override
    public boolean cleansStackTrace() {
        return cleansStackTrace;
    }

    @Override
    public boolean enableClassCache() {
        return enableClassCache;
    }
}