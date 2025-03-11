package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandles;
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
        return new ModuleSystemFound() {
            @Override
            void exportFromTo(Module source, Module target, String packageName) {
                throw new UnsupportedOperationException();
            }

            @Override
            void openFromToRaw(Module source, Module target, String packageName) {
                throw new UnsupportedOperationException();
            }
        };
    }

    static ModuleHandler make(Instrumentation inst) {
        return new ModuleSystemFound() {
            @Override
            void exportFromTo(Module source, Module target, String packageName) {
                inst.redefineModule(
                    source,
                    Set.of(),
                    Map.of(packageName, Set.of(target)),
                    Map.of(),
                    Set.of(),
                    Map.of());
            }

            @Override
            void openFromToRaw(Module source, Module target, String packageName) {
                inst.redefineModule(
                    source,
                    Set.of(),
                    Map.of(),
                    Map.of(packageName, Set.of(target)),
                    Set.of(),
                    Map.of());
            }
        };
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

        private final MethodHandles.Lookup lookup = MethodHandles.lookup();

        @Override
        void exportFromTo(Class<?> source, Class<?> target) {
            exportFromToRaw(source, target.getModule());
        }

        @Override
        void exportFromToRaw(Class<?> source, Object target) {
            if (!source.getModule().isExported(source.getPackageName(), (Module) target)) {
                if (source.getModule() == ModuleHandler.class.getModule()) {
                    source.getModule().addExports(source.getPackageName(), (Module) target);
                } else {
                    exportFromTo(source.getModule(), (Module) target, source.getPackageName());
                }
            }
        }

        abstract void exportFromTo(Module source, Module target, String packageName);

        @Override
        void openFromTo(Class<?> source, Class<?> target) {
            openFromToRaw(source, target.getModule());
        }

        @Override
        void openFromToRaw(Class<?> source, Object target) {
            if (!source.getModule().isOpen(source.getPackageName(), (Module) target)) {
                if (source.getModule() == ModuleHandler.class.getModule()) {
                    source.getModule().addOpens(source.getPackageName(), (Module) target);
                } else {
                    openFromToRaw(source.getModule(), (Module) target, source.getPackageName());
                }
            }
        }

        abstract void openFromToRaw(Module source, Module target, String packageName);

        @Override
        Object toCodegenModule(ClassLoader classLoader) {
            return classLoader.getUnnamedModule();
        }

        @Override
        public ClassLoadingStrategy<ClassLoader> classLoadingStrategy() {
            return ClassLoadingStrategy.UsingLookup.of(lookup);
        }

        @Override
        ClassLoadingStrategy<ClassLoader> classLoadingStrategy(Class<?> type) {
            if (!Mockito.class.getModule().canRead(type.getModule())) {
                Mockito.class.getModule().addReads(type.getModule());
            }
            try {
                return ClassLoadingStrategy.UsingLookup.of(MethodHandles.privateLookupIn(type, lookup));
            } catch (IllegalAccessException e) {
                throw new MockitoException("Could not resolve private lookup for " + type.getTypeName(), e);
            }
        }
    }
}
