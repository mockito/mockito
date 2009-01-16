package org.mockito.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Mockito;
import org.mockito.ReturnValues;
import org.mockito.internal.configuration.Configuration;
import org.mockito.internal.configuration.DefaultAnnotationEngine;
import org.mockitousage.configuration.SmartMock;

@SuppressWarnings("deprecation")
public class MockitoConfiguration extends DefaultMockitoConfiguration implements IMockitoConfiguration {

    private static ReturnValues overriddenReturnValues = null;

    //for testing purposes, allow to override the configuration
    public static void overrideReturnValues(ReturnValues returnValues) {
        MockitoConfiguration.overriddenReturnValues = returnValues;
    }

    @Override
    public ReturnValues getReturnValues() {
        if (overriddenReturnValues == null) {
            return Configuration.instance().getReturnValues();
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
}