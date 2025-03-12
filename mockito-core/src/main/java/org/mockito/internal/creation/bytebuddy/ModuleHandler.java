/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class ModuleHandler {

    abstract void exportFromTo(Class<?> source, Class<?> target);

    abstract void exportFromToRaw(Class<?> source, Object target);

    abstract void openFromTo(Class<?> source, Class<?> target);

    abstract void openFromToRaw(Class<?> source, Object target);

    abstract Object toCodegenModule(ClassLoader classLoader);

    public abstract ClassLoadingStrategy<ClassLoader> classLoadingStrategy();

    abstract ClassLoadingStrategy<ClassLoader> classLoadingStrategy(Class<?> type);

    static ModuleHandler make() {
        try {
            return new ModuleSystemFound() {
                @Override
                void exportFromTo(Object source, Object target, String packageName) {
                    throw new UnsupportedOperationException("Cannot export " + packageName + " of " + source + " to " + target);
                }

                @Override
                void openFromToRaw(Object source, Object target, String packageName) {
                    throw new UnsupportedOperationException("Cannot open " + packageName + " of " + source + " to " + target);
                }
            };
        } catch (Exception ignored) {
            return new NoModuleSystemFound() {
                @Override
                public ClassLoadingStrategy<ClassLoader> classLoadingStrategy() {
                    return Default.INJECTION;
                }
            };
        }
    }

    static ModuleHandler make(Object inst) {
        try {
            Method redefineModule =
                    Class.forName("java.lang.instrument.Instrumentation")
                            .getMethod(
                                    "redefineModule",
                                    Module.class,
                                    Set.class,
                                    Map.class,
                                    Map.class,
                                    Set.class,
                                    Map.class);
            return new ModuleSystemFound() {
                @Override
                void exportFromTo(Object source, Object target, String packageName) {
                    try {
                        redefineModule.invoke(
                                inst,
                                source,
                                Collections.emptySet(),
                                Collections.singletonMap(
                                        packageName, Collections.singleton(target)),
                                Collections.emptyMap(),
                                Collections.emptySet(),
                                Collections.emptyMap());
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }

                @Override
                void openFromToRaw(Object source, Object target, String packageName) {
                    try {
                        redefineModule.invoke(
                                inst,
                                source,
                                Collections.emptySet(),
                                Collections.emptyMap(),
                                Collections.singletonMap(
                                        packageName, Collections.singleton(target)),
                                Collections.emptySet(),
                                Collections.emptyMap());
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            };
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public abstract static class NoModuleSystemFound extends ModuleHandler {

        @Override
        void exportFromTo(Class<?> source, Class<?> target) {
            // empty
        }

        @Override
        void exportFromToRaw(Class<?> source, Object target) {
            // empty

        }

        @Override
        void openFromTo(Class<?> source, Class<?> target) {
            // empty

        }

        @Override
        void openFromToRaw(Class<?> source, Object target) {
            // empty
        }

        @Override
        Object toCodegenModule(ClassLoader classLoader) {
            return null;
        }

        @Override
        ClassLoadingStrategy<ClassLoader> classLoadingStrategy(Class<?> type) {
            return classLoadingStrategy();
        }
    }

    abstract static class ModuleSystemFound extends ModuleHandler {

        private final Object lookup;
        private final Method privateLookupIn,
                getModule,
                isExported,
                isOpen,
                canRead,
                addExports,
                addOpens,
                addReads,
                getPackageName,
                getUnnamedModule;

        public ModuleSystemFound() throws Exception {
            Class<?> methodHandles = Class.forName("java.lang.invoke.MethodHandles");
            lookup = methodHandles.getMethod("lookup").invoke(null);
            privateLookupIn =
                    methodHandles.getMethod(
                            "privateLookupIn",
                            Class.class,
                            Class.forName("java.lang.invoke.MethodHandles$Lookup"));
            getModule = Class.class.getMethod("getModule");
            Class<?> module = Class.forName("java.lang.Module");
            isExported = module.getMethod("isExported", String.class, module);
            isOpen = module.getMethod("isOpen", String.class, module);
            canRead = module.getMethod("canRead", module);
            addExports = module.getMethod("addExports", String.class, module);
            addOpens = module.getMethod("addOpens", String.class, module);
            addReads = module.getMethod("addReads", module);
            getPackageName = Class.class.getMethod("getPackageName");
            getUnnamedModule = ClassLoader.class.getMethod("getUnnamedModule");
        }

        private Object getModule(Class<?> type) {
            try {
                return getModule.invoke(type);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private boolean isExported(Object module, String packageName, Object target) {
            try {
                return (Boolean) isExported.invoke(module, packageName, target);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private boolean isOpen(Object module, String packageName, Object target) {
            try {
                return (Boolean) isOpen.invoke(module, packageName, target);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private boolean canRead(Object module, Object target) {
            try {
                return (Boolean) canRead.invoke(module, target);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private void addExports(Object module, String packageName, Object target) {
            try {
                addExports.invoke(module, packageName, target);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private void addOpens(Object module, String packageName, Object target) {
            try {
                addOpens.invoke(module, packageName, target);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private void addReads(Object module, Object target) {
            try {
                addReads.invoke(module, target);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private String getPackageName(Class<?> type) {
            try {
                return (String) getPackageName.invoke(type);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        void exportFromTo(Class<?> source, Class<?> target) {
            exportFromToRaw(source, getModule(target));
        }

        @Override
        void exportFromToRaw(Class<?> source, Object target) {
            if (!isExported(getModule(source), getPackageName(source), target)) {
                if (getModule(source) == getModule(ModuleHandler.class)) {
                    addExports(getModule(source), getPackageName(source), target);
                } else {
                    exportFromTo(getModule(source), target, getPackageName(source));
                }
            }
        }

        abstract void exportFromTo(Object source, Object target, String packageName);

        @Override
        void openFromTo(Class<?> source, Class<?> target) {
            openFromToRaw(source, getModule(target));
        }

        @Override
        void openFromToRaw(Class<?> source, Object target) {
            if (!isOpen(getModule(source), getPackageName(source), target)) {
                if (getModule(source) == getModule(ModuleHandler.class)) {
                    addOpens(getModule(source), getPackageName(source), target);
                } else {
                    openFromToRaw(getModule(source), target, getPackageName(source));
                }
            }
        }

        abstract void openFromToRaw(Object source, Object target, String packageName);

        @Override
        Object toCodegenModule(ClassLoader classLoader) {
            try {
                return getUnnamedModule.invoke(classLoader);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public ClassLoadingStrategy<ClassLoader> classLoadingStrategy() {
            return ClassLoadingStrategy.UsingLookup.of(lookup);
        }

        @Override
        ClassLoadingStrategy<ClassLoader> classLoadingStrategy(Class<?> type) {
            if (!canRead(getModule(Mockito.class), getModule(type))) {
                addReads(getModule(Mockito.class), getModule(type));
            }
            try {
                return ClassLoadingStrategy.UsingLookup.of(
                        privateLookupIn.invoke(null, type, lookup));
            } catch (Exception e) {
                throw new MockitoException(
                        "Could not resolve private lookup for " + type.getTypeName(), e);
            }
        }
    }
}
