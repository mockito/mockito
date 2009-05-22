/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Mockito;
import org.mockito.internal.configuration.DefaultAnnotationEngine;
import org.mockito.stubbing.Answer;
import org.mockitousage.configuration.SmartMock;

public class MockitoConfiguration extends DefaultMockitoConfiguration implements IMockitoConfiguration {

    private Answer<Object> overriddenReturnValues = null;
    private boolean cleansStackTrace;

    //for testing purposes, allow to override the configuration
    public void overrideReturnValues(Answer<Object> returnValues) {
        this.overriddenReturnValues = returnValues;
    }

    //for testing purposes, allow to override the configuration
    public void overrideCleansStackTrace(boolean cleansStackTrace) {
        this.cleansStackTrace = cleansStackTrace;
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        if (overriddenReturnValues == null) {
            return super.getDefaultAnswer();
        } else {
            return overriddenReturnValues;
        }
    }
    
    @Override
    public AnnotationEngine getAnnotationEngine() {
        return new DefaultAnnotationEngine() {
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
}