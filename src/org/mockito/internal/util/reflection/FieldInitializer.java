package org.mockito.internal.util.reflection;

import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Initialize a field with type instance if a default constructor can be found.
 *
 * <p>
 * If the given field is already initialize
 * This initializer doesn't work with inner classes, local classes, interfaces or abstract types.
 * </p>
 *
 */
public class FieldInitializer {

    private Object testClass;
    private Field field;


    /**
     * Initialize the given field on the given instance.
     *
     * <p>
     * This constructor fail fast if the field type cannot be handled.
     * </p>
     *
     * @param testClass Instance of the test.
     * @param field Field to be initialize.
     */
    public FieldInitializer(Object testClass, Field field) {
        if(new FieldReader(testClass, field).isNull()) {
            checkNotLocal(field);
            checkNotInner(field);
            checkNotInterface(field);
            checkNotAbstract(field);
        }
        this.testClass = testClass;
        this.field = field;
    }

    public Object initialize() {
        final AccessibilityChanger changer = new AccessibilityChanger();
        changer.enableAccess(field);

        try {
            return acquireFieldInstance(testClass, field);
        } catch(IllegalAccessException e) {
            throw new MockitoException("Problems injecting dependencies in " + field.getName(), e);
        } finally {
            changer.safelyDisableAccess(field);
        }
    }

    private void initializeField(Object testClass, Field field) {
        final AccessibilityChanger changer = new AccessibilityChanger();
        Constructor<?> constructor = null;
        try {
            constructor = field.getType().getDeclaredConstructor();
            changer.enableAccess(constructor);

            final Object[] noArg = new Object[0];
            Object newFieldInstance = constructor.newInstance(noArg);
            new FieldSetter(testClass, field).set(newFieldInstance);
        } catch (NoSuchMethodException e) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' has no default constructor", e);
        } catch (InvocationTargetException e) {
            throw new MockitoException("the default constructor of type '" + field.getType().getSimpleName() + "' has raised an exception (see the stack trace for cause): " + e.getTargetException().toString(), e);
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

    private void checkNotLocal(Field field) {
        if(field.getType().isLocalClass()) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is a local class.");
        }
    }

    private void checkNotInner(Field field) {
        if(field.getType().isMemberClass() && !Modifier.isStatic(field.getType().getModifiers())) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is an inner class.");
        }
    }

    private void checkNotInterface(Field field) {
        if(field.getType().isInterface()) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + "' is an interface.");
        }
    }

    private void checkNotAbstract(Field field) {
        if(Modifier.isAbstract(field.getType().getModifiers())) {
            throw new MockitoException("the type '" + field.getType().getSimpleName() + " is an abstract class.");
        }
    }

    private Object acquireFieldInstance(Object testClass, Field field) throws IllegalAccessException {
        Object fieldInstance = field.get(testClass);
        if(fieldInstance != null) {
            return fieldInstance;
        }

        initializeField(testClass, field);
        return field.get(testClass);
    }
}
