/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import net.bytebuddy.ClassFileVersion;
import org.mockito.plugins.MemberAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModuleMemberAccessor implements MemberAccessor {

    private final MemberAccessor delegate;

    public ModuleMemberAccessor() {
        if (ClassFileVersion.ofThisVm().isAtLeast(ClassFileVersion.JAVA_V9)) {
            delegate = new InstrumentationMemberAccessor();
        } else {
            delegate = new ReflectionMemberAccessor();
        }
    }

    @Override
    public Object newInstance(Constructor<?> constructor, Object... arguments)
            throws InstantiationException, InvocationTargetException, IllegalAccessException {
        return delegate.newInstance(constructor, arguments);
    }

    @Override
    public Object newInstance(
            Constructor<?> constructor, OnConstruction onConstruction, Object... arguments)
            throws InstantiationException, InvocationTargetException, IllegalAccessException {
        return delegate.newInstance(constructor, onConstruction, arguments);
    }

    @Override
    public Object invoke(Method method, Object target, Object... arguments)
            throws InvocationTargetException, IllegalAccessException {
        return delegate.invoke(method, target, arguments);
    }

    @Override
    public Object get(Field field, Object target) throws IllegalAccessException {
        return delegate.get(field, target);
    }

    @Override
    public void set(Field field, Object target, Object value) throws IllegalAccessException {
        delegate.set(field, target, value);
    }
}
