/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import static org.mockito.Mockito.withSettings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

/**
 * Process fields annotated with &#64;Spy.
 *
 * <p>
 * Will try transform the field in a spy as with <code>Mockito.spy()</code>.
 * </p>
 *
 * <p>
 * If the field is not initialized, will try to initialize it, with a no-arg constructor.
 * </p>
 *
 * <p>
 * If the field is also annotated with the <strong>compatible</strong> &#64;InjectMocks then the field will be ignored,
 * The injection engine will handle this specific case.
 * </p>
 *
 * <p>This engine will fail, if the field is also annotated with incompatible Mockito annotations.
 */
@SuppressWarnings({"unchecked"})
public class SpyAnnotationEngine implements AnnotationEngine {

    public Object createMockFor(Annotation annotation, Field field) {
        return null;
    }

    @SuppressWarnings("deprecation") // for MockitoAnnotations.Mock
    public void process(Class<?> context, Object testInstance) {
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class) && !field.isAnnotationPresent(InjectMocks.class)) {
                assertNoIncompatibleAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
                field.setAccessible(true);
                Object instance = null;
                try {
                    instance = field.get(testInstance);
                    if (new MockUtil().isMock(instance)) {
                        // instance has been spied earlier
                        // for example happens when MockitoAnnotations.initMocks is called two times.
                        Mockito.reset(instance);
                    } else if (instance != null) {
                        field.set(testInstance, Mockito.mock(instance.getClass(), withSettings()
                                .spiedInstance(instance)
                                .defaultAnswer(Mockito.CALLS_REAL_METHODS)
                                .name(field.getName())));
                    } else {
                    	field.set(testInstance, newSpyInstance(testInstance, field));
                    }
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Problems initiating spied field " + field.getName(), e);
                } catch (InstantiationException e) {
                    throw new MockitoException("Problems initiating spied field " + field.getName(), e);
                } catch (InvocationTargetException e) {
                    throw new MockitoException("Problems initiating spied field " + field.getName(), e);
                }
            }
        }
    }
  
    private static Object newSpyInstance(Object testInstance, Field field)
    		throws InstantiationException, IllegalAccessException, InvocationTargetException {
    	MockSettings settings = withSettings()
    			.defaultAnswer(Mockito.CALLS_REAL_METHODS)
    			.name(field.getName());
    	Class<?> type = field.getType();
    	if (type.isInterface()) {
    		return Mockito.mock(type,settings.useConstructor());
    	}
    	try {
	    	if (!Modifier.isStatic(type.getModifiers())) {
	        	Class<?> enclosing = type.getEnclosingClass();
	        	if (enclosing != null) {
	        		if (!enclosing.isInstance(testInstance)) {
	        			throw new MockitoException("Cannot spy inner " + type);
	        		}
	        		if (Modifier.isPrivate(type.getDeclaredConstructor(enclosing).getModifiers())) {
	        			throw new MockitoException("Cannot spy inner " + type + " with private constructor");
	        		}
	        		return Mockito.mock(type, settings
	        				.useConstructor()
	        				.outerInstance(testInstance));
	        	}
	    	}
	    	Constructor<?> constructor = type.getDeclaredConstructor();
	    	if (Modifier.isPrivate(constructor.getModifiers())) {
	    		constructor.setAccessible(true);
	    		return Mockito.mock(type, settings
	    				.spiedInstance(constructor.newInstance()));
	    	} else {
	    		return Mockito.mock(type, settings.useConstructor());
	    	}
    	} catch (NoSuchMethodException noDefaultConstructor) {
    		throw new MockitoException("0-arg constructor is required to spy " + type);
    	}
    }
    
    //TODO duplicated elsewhere
    void assertNoIncompatibleAnnotations(Class annotation, Field field, Class... undesiredAnnotations) {
        for (Class u : undesiredAnnotations) {
            if (field.isAnnotationPresent(u)) {
                new Reporter().unsupportedCombinationOfAnnotations(annotation.getSimpleName(), annotation.getClass().getSimpleName());
            }
        }        
    }    
}
