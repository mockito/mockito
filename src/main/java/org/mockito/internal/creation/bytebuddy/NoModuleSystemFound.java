package org.mockito.internal.creation.bytebuddy;

import org.mockito.codegen.InjectionBase;

public class NoModuleSystemFound extends ModuleHandler {

    @Override
    boolean isOpened(Class<?> source, Class<?> target) {
        return true;
    }

    @Override
    boolean canRead(Class<?> source, Class<?> target) {
        return true;
    }

    @Override
    boolean isExported(Class<?> source) {
        return true;
    }

    @Override
    boolean isExported(Class<?> source, Class<?> target) {
        return true;
    }

    Class<?> injectionBase(ClassLoader classLoader, String tyoeName) {
        return InjectionBase.class;
    }

    @Override
    void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read) {
        // empty
    }
}
