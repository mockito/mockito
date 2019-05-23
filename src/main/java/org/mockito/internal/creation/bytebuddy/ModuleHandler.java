/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.StubMethod;
import org.mockito.codegen.InjectionBase;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import static net.bytebuddy.matcher.ElementMatchers.isTypeInitializer;
import static org.mockito.internal.util.StringUtil.join;

abstract class ModuleHandler {

    abstract boolean isOpened(Class<?> source, Class<?> target);

    abstract boolean canRead(Class<?> source, Class<?> target);

    abstract boolean isExported(Class<?> source);

    abstract boolean isExported(Class<?> source, Class<?> target);

    abstract Class<?> injectionBase(ClassLoader classLoader, String tyoeName);

    abstract void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read);

    static ModuleHandler make(ByteBuddy byteBuddy, SubclassLoader loader, Random random) {
        try {
            return new ModuleSystemFound(
                byteBuddy, loader, random
            );
        } catch (Exception ignored) {
            return new NoModuleSystemFound();
        }
    }

    private static class ModuleSystemFound extends ModuleHandler {

        private final ByteBuddy byteBuddy;
        private final SubclassLoader loader;
        private final Random random;

        private final int injectonBaseSuffix;

        private final Method getModule, isOpen, isExported, isExportedUnqualified, canRead, addExports, addReads, addOpens, forName;

        private ModuleSystemFound(ByteBuddy byteBuddy, SubclassLoader loader, Random random) throws Exception {
            this.byteBuddy = byteBuddy;
            this.loader = loader;
            this.random = random;
            injectonBaseSuffix = Math.abs(random.nextInt());
            Class<?> moduleType = Class.forName("java.lang.Module");
            getModule = Class.class.getMethod("getModule");
            isOpen = moduleType.getMethod("isOpen", String.class, moduleType);
            isExported = moduleType.getMethod("isExported", String.class, moduleType);
            isExportedUnqualified = moduleType.getMethod("isExported", String.class);
            canRead = moduleType.getMethod("canRead", moduleType);
            addExports = moduleType.getMethod("addExports", String.class, moduleType);
            addReads = moduleType.getMethod("addReads", moduleType);
            addOpens = moduleType.getMethod("addOpens", String.class, moduleType);
            forName = Class.class.getMethod("forName", String.class);
        }

        @Override
        boolean isOpened(Class<?> source, Class<?> target) {
            if (source.getPackage() == null) {
                return true;
            }
            return (Boolean) invoke(isOpen, invoke(getModule, source), source.getPackage().getName(), invoke(getModule, target));
        }

        @Override
        boolean canRead(Class<?> source, Class<?> target) {
            return (Boolean) invoke(canRead, invoke(getModule, source), invoke(getModule, target));
        }

        @Override
        boolean isExported(Class<?> source) {
            if (source.getPackage() == null) {
                return true;
            }
            return (Boolean) invoke(isExportedUnqualified, invoke(getModule, source), source.getPackage().getName());
        }

        @Override
        boolean isExported(Class<?> source, Class<?> target) {
            if (source.getPackage() == null) {
                return true;
            }
            return (Boolean) invoke(isExported, invoke(getModule, source), source.getPackage().getName(), invoke(getModule, target));
        }

        @Override
        Class<?> injectionBase(ClassLoader classLoader, String typeName) {
            String packageName = typeName.substring(0, typeName.lastIndexOf('.'));
            if (classLoader == InjectionBase.class.getClassLoader() && InjectionBase.class.getPackage().getName().equals(packageName)) {
                return InjectionBase.class;
            } else {
                synchronized (this) {
                    String name;
                    int suffix = injectonBaseSuffix;
                    do {
                        name = packageName + "." + InjectionBase.class.getSimpleName() + "$" + suffix++;
                        try {
                            Class<?> type = Class.forName(name, false, classLoader);
                            // The injected type must be defined in the class loader that is target of the injection. Otherwise,
                            // the class's unnamed module would differ from the intended module. To avoid conflicts, we increment
                            // the suffix until we hit a class with a known name and generate one if it does not exist.
                            if (type.getClassLoader() == classLoader) {
                                return type;
                            }
                        } catch (ClassNotFoundException ignored) {
                            break;
                        }
                    } while (true);
                    return byteBuddy.subclass(Object.class, ConstructorStrategy.Default.NO_CONSTRUCTORS)
                        .name(name)
                        .make()
                        .load(classLoader, loader.resolveStrategy(InjectionBase.class, classLoader, false))
                        .getLoaded();
                }
            }
        }

        @Override
        void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read) {
            boolean needsExport = export && !isExported(source, target);
            boolean needsRead = read && !canRead(source, target);
            if (!needsExport && !needsRead) {
                return;
            }
            ClassLoader classLoader = source.getClassLoader();
            if (classLoader == null) {
                throw new MockitoException(join("Cannot adjust module graph for modules in the bootstrap loader",
                    "",
                    source + " is declared by the bootstrap loader and cannot be adjusted",
                    "Requires package export to " + target + ": " + needsExport,
                    "Requires adjusted reading of " + target + ": " + needsRead));
            }
            boolean targetVisible = classLoader == target.getClassLoader();
            while (!targetVisible && classLoader != null) {
                classLoader = classLoader.getParent();
                targetVisible = classLoader == target.getClassLoader();
            }
            MethodCall targetLookup;
            Implementation.Composable implementation;
            if (targetVisible) {
                targetLookup = MethodCall.invoke(getModule).onMethodCall(MethodCall.invoke(forName).with(target.getName()));
                implementation = StubMethod.INSTANCE;
            } else {
                Class<?> intermediate;
                Field field;
                try {
                    intermediate = byteBuddy.subclass(Object.class, ConstructorStrategy.Default.NO_CONSTRUCTORS)
                        .name(String.format("%s$%d", "org.mockito.codegen.MockitoTypeCarrier", Math.abs(random.nextInt())))
                        .defineField("mockitoType", Class.class, Visibility.PUBLIC, Ownership.STATIC)
                        .make()
                        .load(source.getClassLoader(), loader.resolveStrategy(source, source.getClassLoader(), false))
                        .getLoaded();
                    field = intermediate.getField("mockitoType");
                    field.set(null, target);
                } catch (Exception e) {
                    throw new MockitoException(join("Could not create a carrier for making the Mockito type visible to " + source,
                        "",
                        "This is required to adjust the module graph to enable mock creation"), e);
                }
                targetLookup = MethodCall.invoke(getModule).onField(field);
                implementation = MethodCall.invoke(getModule).onMethodCall(MethodCall.invoke(forName).with(intermediate.getName()));
            }
            MethodCall sourceLookup = MethodCall.invoke(getModule).onMethodCall(MethodCall.invoke(forName).with(source.getName()));
            if (needsExport) {
                implementation = implementation.andThen(MethodCall.invoke(addExports)
                    .onMethodCall(sourceLookup)
                    .with(target.getPackage().getName())
                    .withMethodCall(targetLookup));
            }
            if (needsRead) {
                implementation = implementation.andThen(MethodCall.invoke(addReads)
                    .onMethodCall(sourceLookup)
                    .withMethodCall(targetLookup));
            }
            try {
                Class.forName(byteBuddy.subclass(Object.class)
                    .name(String.format("%s$%s$%d", source.getName(), "MockitoModuleProbe", Math.abs(random.nextInt())))
                    .invokable(isTypeInitializer()).intercept(implementation)
                    .make()
                    .load(source.getClassLoader(), loader.resolveStrategy(source, source.getClassLoader(), false))
                    .getLoaded()
                    .getName(), true, source.getClassLoader());
            } catch (Exception e) {
                throw new MockitoException(join("Could not force module adjustment of the module of " + source,
                    "",
                    "This is required to adjust the module graph to enable mock creation"), e);
            }
        }

        private static Object invoke(Method method, Object target, Object... args) {
            try {
                return method.invoke(target, args);
            } catch (Exception e) {
                throw new MockitoException(join("Could not invoke " + method + " using reflection",
                    "",
                    "Mockito attempted to interact with the Java module system but an unexpected method behavior was encountered"), e);
            }
        }
    }

    private static class NoModuleSystemFound extends ModuleHandler {

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

        @Override
        Class<?> injectionBase(ClassLoader classLoader, String tyoeName) {
            return InjectionBase.class;
        }

        @Override
        void adjustModuleGraph(Class<?> source, Class<?> target, boolean export, boolean read) {
            // empty
        }
    }
}
