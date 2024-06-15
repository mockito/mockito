/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;

public abstract class ModuleHandler {

    abstract boolean isOpened(Class<?> source, Class<?> target);

    abstract boolean canRead(Class<?> source, Class<?> target);

    abstract boolean isExported(Class<?> source);

    abstract boolean isExported(Class<?> source, Class<?> target);

    abstract Class<?> injectionBase(ClassLoader classLoader, String tyoeName);

    abstract void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read);

    static ModuleHandler make(ByteBuddy byteBuddy, SubclassLoader loader) {
        try {
            return new ModuleSystemFound(byteBuddy, loader);
        } catch (Exception ignored) {
            return new NoModuleSystemFound();
        }
    }
}
