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

import org.mockito.creation.instance.Instantiator;
import org.mockito.creation.instance.InstantiationException;
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
                    evaluateConstructor(matchingConstructors, constructor);
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
                "If you believe that Mockito could do a better job deciding on which constructor to use, please let us know.",
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

    /**
     * Evalutes {@code constructor} against the currently found {@code matchingConstructors} and determines if
     * it's a better match to the given arguments, a worse match, or an equivalently good match.
     * <p>
     * This method tries to emulate the behavior specified in
     * <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.12.2">JLS 15.12.2. Compile-Time
     * Step 2: Determine Method Signature</a>. A constructor X is deemed to be a better match than constructor Y to the
     * given argument list if they are both applicable, constructor X has at least one parameter than is more specific
     * than the corresponding parameter of constructor Y, and constructor Y has no parameter than is more specific than
     * the corresponding parameter in constructor X.
     * </p>
     * <p>
     * If {@code constructor} is a better match than the constructors in the {@code matchingConstructors} list, the list
     * is cleared, and it's added to the list as a singular best matching constructor (so far).<br/>
     * If {@code constructor} is an equivalently good of a match as the constructors in the {@code matchingConstructors}
     * list, it's added to the list.<br/>
     * If {@code constructor} is a worse match than the constructors in the {@code matchingConstructors} list, the list
     * will remain unchanged.
     * </p>
     *
     * @param matchingConstructors A list of equivalently best matching constructors found so far
     * @param constructor The constructor to be evaluated against this list
     */
    private void evaluateConstructor(List<Constructor<?>> matchingConstructors, Constructor<?> constructor) {
        boolean newHasBetterParam = false;
        boolean existingHasBetterParam = false;

        Class<?>[] paramTypes = constructor.getParameterTypes();
        for (int i = 0; i < paramTypes.length; ++i) {
            Class<?> paramType = paramTypes[i];
            if (!paramType.isPrimitive()) {
                for (Constructor<?> existingCtor : matchingConstructors) {
                    Class<?> existingParamType = existingCtor.getParameterTypes()[i];
                    if (paramType != existingParamType) {
                        if (paramType.isAssignableFrom(existingParamType)) {
                            existingHasBetterParam = true;
                        } else {
                            newHasBetterParam = true;
                        }
                    }
                }
            }
        }
        if (!existingHasBetterParam) {
            matchingConstructors.clear();
        }
        if (newHasBetterParam || !existingHasBetterParam) {
            matchingConstructors.add(constructor);
        }
    }
}
