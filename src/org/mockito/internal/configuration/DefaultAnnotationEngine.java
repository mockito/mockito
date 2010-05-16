/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.GenericMaster;

/**
 * Initializes fields annotated with &#64;{@link org.mockito.Mock} or &#64;{@link org.mockito.Captor}.
 * <p/>
 * See {@link MockitoAnnotations}
 */
@SuppressWarnings("unchecked")
public class DefaultAnnotationEngine implements AnnotationEngine {

    /* (non-Javadoc)
    * @see org.mockito.AnnotationEngine#createMockFor(java.lang.annotation.Annotation, java.lang.reflect.Field)
    */
    @SuppressWarnings("deprecation")
    public Object createMockFor(Annotation annotation, Field field) {
        if (annotation instanceof Mock) {
            return processAnnotationOn((Mock) annotation, field);
        }
        if (annotation instanceof MockitoAnnotations.Mock) {
            return processAnnotationOn((MockitoAnnotations.Mock) annotation, field);
        }
        if (annotation instanceof Captor) {
            return processAnnotationOn((Captor) annotation, field);
        }        

        return null;
    }
    
    private Object processAnnotationOn(Mock annotation, Field field) {
        MockSettings mockSettings = Mockito.withSettings();
        if (annotation.extraInterfaces().length > 0) { // never null
            mockSettings.extraInterfaces(annotation.extraInterfaces());
        }
        if ("".equals(annotation.name())) {
            mockSettings.name(field.getName());
        } else {
            mockSettings.name(annotation.name());
        }

        // see @Mock answer default value
        mockSettings.defaultAnswer(annotation.answer().get());
        return Mockito.mock(field.getType(), mockSettings);
    }

    @SuppressWarnings("deprecation")
    private Object processAnnotationOn(org.mockito.MockitoAnnotations.Mock annotation, Field field) {
        return Mockito.mock(field.getType(), field.getName());
    }
    
    private Object processAnnotationOn(Captor annotation, Field field) {
        Class<?> type = field.getType();
        if (!ArgumentCaptor.class.isAssignableFrom(type)) {
            throw new MockitoException("@Captor field must be of the type ArgumentCaptor.\n" + "Field: '"
                    + field.getName() + "' has wrong type\n"
                    + "For info how to use @Captor annotations see examples in javadoc for MockitoAnnotations class.");
        }
        Class cls = new GenericMaster().getGenericType(field);        
        return ArgumentCaptor.forClass(cls);    
    }           

    public void process(Class<?> clazz, Object testClass) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean alreadyAssigned = false;
            for(Annotation annotation : field.getAnnotations()) {           
                Object mock = createMockFor(annotation, field);
                if (mock != null) {
                    throwIfAlreadyAssigned(field, alreadyAssigned);                    
                    alreadyAssigned = true;                    
                    try {
                        new FieldSetter(testClass, field).set(mock);
                    } catch (Exception e) {
                        throw new MockitoException("Problems setting field " + field.getName() + " annotated with "
                                + annotation, e);
                    }
                }        
            }
        }
    }
    
    void throwIfAlreadyAssigned(Field field, boolean alreadyAssigned) {
        if (alreadyAssigned) {
            new Reporter().moreThanOneAnnotationNotAllowed(field.getName());
        }
    }
}