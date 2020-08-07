/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.moduletest;

import org.junit.Test;
import org.mockito.internal.util.reflection.ModuleMemberAccessor;
import org.mockito.internal.util.reflection.ReflectionMemberAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.moduletest.ModuleUtil.layer;
import static org.mockito.moduletest.ModuleUtil.modularJar;

public class ModuleAccessTest {

    @Test
    public void can_access_non_opened_module_with_module_member_accessor() throws Exception {
        Path jar = modularJar(false, false, false);
        ModuleLayer layer = layer(jar, false, true);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> moduleMemberAccessor = loader.loadClass(ModuleMemberAccessor.class.getName());
            Object instance = moduleMemberAccessor.getConstructor().newInstance();
            @SuppressWarnings("unchecked")
            Callable<String> mock = (Callable<String>) moduleMemberAccessor
                .getMethod("newInstance", Constructor.class, Object[].class)
                .invoke(instance, type.getConstructor(), new Object[0]);
            assertThat(mock.call()).isEqualTo("foo");
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }

    @Test
    public void cannot_access_non_opened_module_with_reflection_member_accessor() throws Exception {
        Path jar = modularJar(false, false, false);
        ModuleLayer layer = layer(jar, false, true);

        ClassLoader loader = layer.findLoader("mockito.test");
        Class<?> type = loader.loadClass("sample.MyCallable");

        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(loader);
        try {
            Class<?> moduleMemberAccessor = loader.loadClass(ReflectionMemberAccessor.class.getName());
            try {
                Object instance = moduleMemberAccessor.getConstructor().newInstance();
                moduleMemberAccessor
                    .getMethod("newInstance", Constructor.class, Object[].class)
                    .invoke(instance, type.getConstructor(), new Object[0]);
                fail();
            } catch (InvocationTargetException e) {
                assertThat(e.getCause()).isInstanceOf(IllegalAccessException.class);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(contextLoader);
        }
    }
}
