/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Mockito;
import org.mockito.internal.configuration.InjectingAnnotationEngine;
import org.mockito.stubbing.Answer;
import org.mockitousage.configuration.SmartMock;

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
            public Object createMockFor(Annotation annotation, Field field) {
                if (annotation instanceof SmartMock) {
                    return Mockito.mock(field.getType(), Mockito.RETURNS_SMART_NULLS);
                } else {
                    return super.createMockFor(annotation, field);
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