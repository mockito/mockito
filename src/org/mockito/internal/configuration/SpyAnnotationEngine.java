/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

@SuppressWarnings({"deprecation", "unchecked"})
public class SpyAnnotationEngine implements AnnotationEngine {

    @Override
    public Object createMockFor(Annotation annotation, Field field) {
        return null;
    }
    
    @Override
    public void process(Class<?> context, Object testClass) {
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
                boolean wasAccessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    Object instance = field.get(testClass);
                    if (instance == null) {
                        throw new MockitoException("Cannot create a @Spy for '" + field.getName() + "' field because the *instance* is missing\n" +
                        		  "The instance must be created *before* initMocks();\n" +
                                  "Example of correct usage of @Spy:\n" +
                            	  "   @Spy List mock = new LinkedList();\n" +
                            	  "   //also, don't forget about MockitoAnnotations.initMocks();");

                    }
                    if (new MockUtil().isMock(instance)) { 
                        // instance has been spied earlier
                        Mockito.reset(instance);
                    } else {
                        field.set(testClass, Mockito.spy(instance));
                    }
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Problems initiating spied field " + field.getName(), e);
                } finally {
                    field.setAccessible(wasAccessible);
                }
            }
        }
    }
    
    //TODO duplicated elsewhere
    void assertNoAnnotations(Class annotation, Field field, Class ... undesiredAnnotations) {
        for (Class u : undesiredAnnotations) {
            if (field.isAnnotationPresent(u)) {
                new Reporter().unsupportedCombinationOfAnnotations(annotation.getSimpleName(), annotation.getClass().getSimpleName());
            }
        }        
    }    
}
