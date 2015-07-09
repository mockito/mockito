/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * This utility class will call the setter of the property to inject a new value.
 */
public class BeanPropertySetter {

    private static final String SET_PREFIX = "set";

    private final Object target;
    private final boolean reportNoSetterFound;
    private final Field field;

    /**
     * New BeanPropertySetter
     * @param target The target on which the setter must be invoked
     * @param propertyField The field that should be accessed with the setter
     * @param reportNoSetterFound Allow the set method to raise an Exception if the setter cannot be found
     */
    public BeanPropertySetter(final Object target, final Field propertyField, boolean reportNoSetterFound) {
        this.field = propertyField;
        this.target = target;
        this.reportNoSetterFound = reportNoSetterFound;
    }

    /**
     * New BeanPropertySetter that don't report failure
     * @param target The target on which the setter must be invoked
     * @param propertyField The propertyField that must be accessed through a setter
     */
    public BeanPropertySetter(final Object target, final Field propertyField) {
        this(target, propertyField, false);
    }

    /**
     * Set the value to the property represented by this {@link BeanPropertySetter}
     * @param value the new value to pass to the property setter
     * @return <code>true</code> if the value has been injected, <code>false</code> otherwise
     * @throws RuntimeException Can be thrown if the setter threw an exception, if the setter is not accessible
     *          or, if <code>reportNoSetterFound</code> and setter could not be found.
     */
    public boolean set(final Object value) {

        AccessibilityChanger changer = new AccessibilityChanger();
        Method writeMethod = null;
        try {
            writeMethod = target.getClass().getMethod(setterName(field.getName()), field.getType());

            changer.enableAccess(writeMethod);
            writeMethod.invoke(target, value);
            return true;
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Setter '" + writeMethod + "' of '" + target + "' with value '" + value + "' threw exception : '" + e.getTargetException() + "'", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Access not authorized on field '" + field + "' of object '" + target + "' with value: '" + value + "'", e);
        } catch (NoSuchMethodException e) {
            reportNoSetterFound();
        } finally {
            if(writeMethod != null) {
                changer.safelyDisableAccess(writeMethod);
            }
        }

        reportNoSetterFound();
        return false;
    }

    /**
     * Retrieve the setter name from the field name.
     *
     * <p>Implementation is based on the code of {@link java.beans.Introspector}.</p>
     *
     * @param fieldName the Field name
     * @return Setter name.
     */
    private String setterName(String fieldName) {
        return new StringBuilder(SET_PREFIX)
                .append(fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH))
                .append(fieldName.substring(1))
                .toString();
    }

    private void reportNoSetterFound() {
        if(reportNoSetterFound) {
            throw new RuntimeException("Problems setting value on object: [" + target + "] for property : [" + field.getName() + "], setter not found");
        }
    }

}
