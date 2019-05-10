/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.mockito.exceptions.base.MockitoException;

import static java.lang.reflect.Modifier.isStatic;
import static org.mockito.internal.util.reflection.FieldSetter.setField;

/**
 * Initialize a field with type instance if a constructor can be found by the strategy of given
 * {@link ConstructorResolver}.
 *
 * <p>
 * If the given field is already initialized, then <strong>the actual instance is returned</strong>.
 * This initializer doesn't work with inner classes, local classes, interfaces or abstract types.
 * </p>
 *
 */
public class FieldInitializer {

    private final Object fieldOwner;
    private final Field field;
    private final ConstructorResolver constructorResolver;

    /**
     * Prepare initializer with the given field on the given instance.
     *
     * <p>
     * This constructor fail fast if the field type cannot be handled.
     * </p>
     *
     * @param fieldOwner Instance of the test.
     * @param field Field to be initialize.
     * @param constructorResolver Constructor resolver strategy
     */
    public FieldInitializer(Object fieldOwner, Field field, ConstructorResolver constructorResolver) {
        if(new FieldReader(fieldOwner, field).isNull()) {
            checkNotLocal(field);
            checkNotInner(field);
            checkNotInterface(field);
            checkNotEnum(field);
            checkNotAbstract(field);
        }
        this.fieldOwner = fieldOwner;
        this.field = field;
        this.constructorResolver = constructorResolver;
    }

    /**
     * Initialize field if not initialized and return the actual instance.
     *
     * @return Actual field instance.
     */
    public FieldInitializationReport initialize() {
        final AccessibilityChanger changer = new AccessibilityChanger();
        changer.enableAccess(field);

        try {
            return acquireFieldInstance();
        } catch(IllegalAccessException e) {
            throw new MockitoException("Problems initializing field '" + field.getName() + "' of type '" + field.getType().getSimpleName() + "'", e);
        } finally {
            changer.safelyDisableAccess(field);
        }
    }

    private void checkNotLocal(Field field) {
        if(field.getType().isLocalClass()) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is a local class.");
        }
    }

    private void checkNotInner(Field field) {
        Class<?> type = field.getType();
        if(type.isMemberClass() && !isStatic(type.getModifiers())) {
            throw new MockitoException("the type '" + type.getSimpleName() + "' is an inner non static class.");
        }
    }

    private void checkNotInterface(Field field) {
        if(field.getType().isInterface()) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is an interface.");
        }
    }

    private void checkNotAbstract(Field field) {
        if(Modifier.isAbstract(field.getType().getModifiers())) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is an abstract class.");
        }
    }

    private void checkNotEnum(Field field) {
        if(field.getType().isEnum()) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is an enum.");
        }
    }


    private FieldInitializationReport acquireFieldInstance() throws IllegalAccessException {
        Object fieldInstance = field.get(fieldOwner);
        if(fieldInstance != null) {
            return new FieldInitializationReport(fieldInstance, false);
        }

        return instantiate();
    }

    private FieldInitializationReport instantiate() {
        final AccessibilityChanger changer = new AccessibilityChanger();
        Constructor<?> constructor = null;
        try {
            if (!constructorResolver.isResolvable()) {
                return new FieldInitializationReport();
            }

            constructor = constructorResolver.resolveConstructor();
            changer.enableAccess(constructor);

            final Object[] args = constructorResolver.resolveArguments();
            Object newFieldInstance = constructor.newInstance(args);
            setField(fieldOwner, field, newFieldInstance);

            return new FieldInitializationReport(newFieldInstance, true);
        } catch (MockitoException e) {
            throw new MockitoException("field '" + field.getName() + "': " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new MockitoException("internal error : " + constructorResolver.getClass().getSimpleName() + " provided incorrect types for constructor " + constructor + " of type " + field.getType().getSimpleName(), e);
        } catch (InvocationTargetException e) {
            throw new MockitoException("the constructor of type '" + field.getType().getSimpleName() + "' has raised an exception (see the stack trace for cause): " + e.getTargetException().toString(), e);
        } catch (InstantiationException e) {
            throw new MockitoException("InstantiationException (see the stack trace for cause): " + e.toString(), e);
        } catch (IllegalAccessException e) {
            throw new MockitoException("IllegalAccessException (see the stack trace for cause): " + e.toString(), e);
        } finally {
            if(constructor != null) {
                changer.safelyDisableAccess(constructor);
            }
        }
    }

}
