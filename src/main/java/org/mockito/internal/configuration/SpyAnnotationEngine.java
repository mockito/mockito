/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

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
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.plugins.AnnotationEngine;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.withSettings;
import static org.mockito.internal.exceptions.Reporter.unsupportedCombinationOfAnnotations;
import static org.mockito.internal.util.StringUtil.join;

/**
 * Process fields annotated with &#64;Spy.
 * <p/>
 * <p>
 * Will try transform the field in a spy as with <code>Mockito.spy()</code>.
 * </p>
 * <p/>
 * <p>
 * If the field is not initialized, will try to initialize it, with a no-arg constructor.
 * </p>
 * <p/>
 * <p>
 * If the field is also annotated with the <strong>compatible</strong> &#64;InjectMocks then the field will be ignored,
 * The injection engine will handle this specific case.
 * </p>
 * <p/>
 * <p>This engine will fail, if the field is also annotated with incompatible Mockito annotations.
 */
@SuppressWarnings({"unchecked"})
public class SpyAnnotationEngine implements AnnotationEngine, org.mockito.configuration.AnnotationEngine {

    @Override
    public void process(Class<?> context, Object testInstance) {
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class) && !field.isAnnotationPresent(InjectMocks.class)) {
                assertNoIncompatibleAnnotations(Spy.class, field, Mock.class, Captor.class);
                field.setAccessible(true);
                Object instance;
                try {
                    instance = field.get(testInstance);
                    if (MockUtil.isMock(instance)) {
                        // instance has been spied earlier
                        // for example happens when MockitoAnnotations.initMocks is called two times.
                        Mockito.reset(instance);
                    } else if (instance != null) {
                        field.set(testInstance, spyInstance(field, instance));
                    } else {
                        field.set(testInstance, spyNewInstance(testInstance, field));
                    }
                } catch (Exception e) {
                    throw new MockitoException("Unable to initialize @Spy annotated field '" + field.getName() + "'.\n" + e.getMessage(), e);
                }
            }
        }
    }

    private static Object spyInstance(Field field, Object instance) {
        return Mockito.mock(instance.getClass(),
                            withSettings().spiedInstance(instance)
                                                           .defaultAnswer(CALLS_REAL_METHODS)
                                                           .name(field.getName()));
    }

    private static Object spyNewInstance(Object testInstance, Field field)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        MockSettings settings = withSettings().defaultAnswer(CALLS_REAL_METHODS)
                                              .name(field.getName());
        Class<?> type = field.getType();
        if (type.isInterface()) {
            return Mockito.mock(type, settings.useConstructor());
        }
        int modifiers = type.getModifiers();
        if (typeIsPrivateAbstractInnerClass(type, modifiers)) {
            throw new MockitoException(join("@Spy annotation can't initialize private abstract inner classes.",
                                            "  inner class: '" + type.getSimpleName() + "'",
                                            "  outer class: '" + type.getEnclosingClass().getSimpleName() + "'",
                                            "",
                                            "You should augment the visibility of this inner class"));
        }
        if (typeIsNonStaticInnerClass(type, modifiers)) {
            Class<?> enclosing = type.getEnclosingClass();
            if (!enclosing.isInstance(testInstance)) {
                throw new MockitoException(join("@Spy annotation can only initialize inner classes declared in the test.",
                                                "  inner class: '" + type.getSimpleName() + "'",
                                                "  outer class: '" + enclosing.getSimpleName() + "'",
                                                ""));
            }
            return Mockito.mock(type, settings.useConstructor()
                                              .outerInstance(testInstance));
        }

        Constructor<?> constructor = noArgConstructorOf(type);
        if (Modifier.isPrivate(constructor.getModifiers())) {
            constructor.setAccessible(true);
            return Mockito.mock(type, settings.spiedInstance(constructor.newInstance()));
        } else {
            return Mockito.mock(type, settings.useConstructor());
        }
    }

    private static Constructor<?> noArgConstructorOf(Class<?> type) {
        Constructor<?> constructor;
        try {
            constructor = type.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new MockitoException("Please ensure that the type '" + type.getSimpleName() + "' has a no-arg constructor.");
        }
        return constructor;
    }

    private static boolean typeIsNonStaticInnerClass(Class<?> type, int modifiers) {
        return !Modifier.isStatic(modifiers) && type.getEnclosingClass() != null;
    }

    private static boolean typeIsPrivateAbstractInnerClass(Class<?> type, int modifiers) {
        return Modifier.isPrivate(modifiers) && Modifier.isAbstract(modifiers) && type.getEnclosingClass() != null;
    }

    //TODO duplicated elsewhere
    private static void assertNoIncompatibleAnnotations(Class<? extends Annotation> annotation,
                                                        Field field,
                                                        Class<? extends Annotation>... undesiredAnnotations) {
        for (Class<? extends Annotation> u : undesiredAnnotations) {
            if (field.isAnnotationPresent(u)) {
                throw unsupportedCombinationOfAnnotations(annotation.getSimpleName(),
                                                          u.getSimpleName());
            }
        }
    }
}
