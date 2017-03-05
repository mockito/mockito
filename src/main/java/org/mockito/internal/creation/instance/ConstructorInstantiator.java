/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.reflection.AccessibilityChanger;

import static org.mockito.internal.util.StringUtil.join;

public class ConstructorInstantiator implements Instantiator {

    /**
     * Whether or not the constructors used for creating an object refer to an outer instance or not.
     * This member is only used to for constructing error messages.
     * If an outer inject exists, it would be the first ([0]) element of the {@link #constructorArgs} array.
     */
    private final boolean hasOuterClassInstance;
    private final Object[] constructorArgs;

    public ConstructorInstantiator(boolean hasOuterClassInstance, Object... constructorArgs) {
        this.hasOuterClassInstance = hasOuterClassInstance;
        this.constructorArgs = constructorArgs;
    }

    public <T> T newInstance(Class<T> cls) {
        return withParams(cls, constructorArgs);
    }

    private <T> T withParams(Class<T> cls, Object... params) {
        List<Constructor<?>> matchingConstructors = new LinkedList<Constructor<?>>();
        try {
            for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
                Class<?>[] types = constructor.getParameterTypes();
                if (paramsMatch(types, params)) {
                    matchingConstructors.add(constructor);
                }
            }

            if (matchingConstructors.size() == 1) {
                return invokeConstructor(matchingConstructors.get(0), params);
            }
        } catch (Exception e) {
            throw paramsException(cls, e);
        }
        if (matchingConstructors.size() == 0) {
            throw noMatchingConstructor(cls);
        } else {
            throw multipleMatchingConstructors(cls, matchingConstructors);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T invokeConstructor(Constructor<?> constructor, Object... params) throws java.lang.InstantiationException, IllegalAccessException, InvocationTargetException {
        AccessibilityChanger accessibility = new AccessibilityChanger();
        accessibility.enableAccess(constructor);
        return (T) constructor.newInstance(params);
    }

    private InstantiationException paramsException(Class<?> cls, Exception e) {
        return new InstantiationException(join(
                "Unable to create instance of '" + cls.getSimpleName() + "'.",
                "Please ensure the target class has " + constructorArgsString() + " and executes cleanly.")
                , e);
    }

    private String constructorArgTypes() {
        int argPos = 0;
        if (hasOuterClassInstance) {
            ++argPos;
        }
        String[] constructorArgTypes = new String[constructorArgs.length - argPos];
        for (int i = argPos; i < constructorArgs.length; ++i) {
            constructorArgTypes[i - argPos] = constructorArgs[i] == null ? null : constructorArgs[i].getClass().getName();
        }
        return Arrays.toString(constructorArgTypes);
    }

    private InstantiationException noMatchingConstructor(Class<?> cls) {
        String constructorString = constructorArgsString();
        String outerInstanceHint = "";
        if (hasOuterClassInstance) {
            outerInstanceHint = " and provided outer instance is correct";
        }
        return new InstantiationException(join("Unable to create instance of '" + cls.getSimpleName() + "'.",
                "Please ensure that the target class has " + constructorString + outerInstanceHint + ".")
                , null);
    }

    private String constructorArgsString() {
        String constructorString;
        if (constructorArgs.length == 0 || (hasOuterClassInstance && constructorArgs.length == 1)) {
            constructorString = "a 0-arg constructor";
        } else {
            constructorString = "a constructor that matches these argument types: " + constructorArgTypes();
        }
        return constructorString;
    }

    private InstantiationException multipleMatchingConstructors(Class<?> cls, List<Constructor<?>> constructors) {
        return new InstantiationException(join("Unable to create instance of '" + cls.getSimpleName() + "'.",
                "Multiple constructors could be matched to arguments of types " + constructorArgTypes() + ":",
                join("", " - ", constructors),
                "If you believe that Mockito could do a better join deciding on which constructor to use, please let us know.",
                "Ticket 685 contains the discussion and a workaround for ambiguous constructors using inner class.",
                "See https://github.com/mockito/mockito/issues/685"
            ), null);
    }

    private static boolean paramsMatch(Class<?>[] types, Object[] params) {
        if (params.length != types.length) {
            return false;
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                if (types[i].isPrimitive()) {
                    return false;
                }
            } else if ((!types[i].isPrimitive() && !types[i].isInstance(params[i])) ||
                    (types[i].isPrimitive() && !types[i].equals(Primitives.primitiveTypeOf(params[i].getClass())))) {
                return false;
            }
        }
        return true;
    }
}
