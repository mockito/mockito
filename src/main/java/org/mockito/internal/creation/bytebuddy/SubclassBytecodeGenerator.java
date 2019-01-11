/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.SynchronizationState;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.MultipleParentClassLoader;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport.CrossClassLoaderSerializableMock;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor.DispatcherDefaultingToRealMethod;
import org.mockito.mock.SerializableMode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import static java.lang.Thread.currentThread;
import static net.bytebuddy.description.modifier.Visibility.PRIVATE;
import static net.bytebuddy.dynamic.Transformer.ForMethod.withModifiers;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.implementation.attribute.MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.mockito.internal.util.StringUtil.join;

class SubclassBytecodeGenerator implements BytecodeGenerator {

    private static final String CODEGEN_PACKAGE = "org.mockito.codegen.";

    private static final Object MOCKITO_MODULE;
    private static final Method GET_MODULE, IS_OPEN, IS_EXPORTED, CAN_READ, ADD_EXPORTS, ADD_READ, ADD_OPENS, GET_UNNAMED_MODULE;

    static {
        Object mockitoModule;
        Method getModule, isOpen, isExported, canRead, addExports, addRead, addOpens, getUnnamedModule;
        try {
            getModule = Class.class.getMethod("getModule");
            isOpen = getModule.getReturnType().getMethod("isOpen", String.class, getModule.getReturnType());
            canRead = getModule.getReturnType().getMethod("canRead", getModule.getReturnType());
            isExported = getModule.getReturnType().getMethod("isExported", String.class, getModule.getReturnType());
            addExports = getModule.getReturnType().getMethod("addExports", String.class, getModule.getReturnType());
            addRead = getModule.getReturnType().getMethod("addReads", getModule.getReturnType());
            addOpens = getModule.getReturnType().getMethod("addOpens", String.class, getModule.getReturnType());
            getUnnamedModule = ClassLoader.class.getMethod("getUnnamedModule");
            mockitoModule = getModule.invoke(Mockito.class);
        } catch (Throwable ignored) {
            getModule = null;
            isOpen = null;
            isExported = null;
            addExports = null;
            canRead = null;
            addRead = null;
            addOpens = null;
            getUnnamedModule = null;
            mockitoModule = null;
        }
        MOCKITO_MODULE = mockitoModule;
        GET_MODULE = getModule;
        IS_OPEN = isOpen;
        IS_EXPORTED = isExported;
        ADD_EXPORTS = addExports;
        CAN_READ = canRead;
        ADD_OPENS = addOpens;
        GET_UNNAMED_MODULE = getUnnamedModule;
        ADD_READ = addRead;
    }

    private final SubclassLoader loader;

    private final ByteBuddy byteBuddy;
    private final Random random;

    private final Implementation readReplace;
    private final ElementMatcher<? super MethodDescription> matcher;

    private final Implementation dispatcher = to(DispatcherDefaultingToRealMethod.class);
    private final Implementation hashCode = to(MockMethodInterceptor.ForHashCode.class);
    private final Implementation equals = to(MockMethodInterceptor.ForEquals.class);
    private final Implementation writeReplace = to(MockMethodInterceptor.ForWriteReplace.class);

    public SubclassBytecodeGenerator() {
        this(new SubclassInjectionLoader());
    }

    public SubclassBytecodeGenerator(SubclassLoader loader) {
        this(loader, null, any());
    }

    public SubclassBytecodeGenerator(Implementation readReplace, ElementMatcher<? super MethodDescription> matcher) {
        this(new SubclassInjectionLoader(), readReplace, matcher);
    }

    protected SubclassBytecodeGenerator(SubclassLoader loader, Implementation readReplace, ElementMatcher<? super MethodDescription> matcher) {
        this.loader = loader;
        this.readReplace = readReplace;
        this.matcher = matcher;
        byteBuddy = new ByteBuddy().with(TypeValidation.DISABLED);
        random = new Random();
    }

    @Override
    public <T> Class<? extends T> mockClass(MockFeatures<T> features) {
        ClassLoader classLoader = new MultipleParentClassLoader.Builder()
            .appendMostSpecific(features.mockedType)
            .appendMostSpecific(features.interfaces)
            .appendMostSpecific(currentThread().getContextClassLoader())
            .appendMostSpecific(MockAccess.class, DispatcherDefaultingToRealMethod.class)
            .appendMostSpecific(MockMethodInterceptor.class)
            .build(MockMethodInterceptor.class.getClassLoader());
        assertModuleAccessability(features.mockedType);
        for (Class<?> iFace : features.interfaces) {
            assertModuleAccessability(iFace);
        }
        String name = nameFor(features.mockedType, features.interfaces, classLoader);
        if (!name.startsWith(CODEGEN_PACKAGE) && classLoader == features.mockedType.getClassLoader()) {
            loadModuleProble(features.mockedType, Mockito.class, true, !loader.isDisrespectingOpenness(), false);
            for (Class<?> iFace : features.interfaces) {
                loadModuleProble(iFace, features.mockedType, false, false, true);
                loadModuleProble(features.mockedType, iFace, true, false, false);
            }
        } else {
            Class<?> target = classLoader instanceof MultipleParentClassLoader ? loadHookType(classLoader) : Mockito.class;
            assertVisibility(features.mockedType);
            loadModuleProble(features.mockedType, target, false, false, true);
            for (Class<?> iFace : features.interfaces) {
                assertVisibility(iFace);
                loadModuleProble(iFace, target, false, false, true);
            }
        }
        DynamicType.Builder<T> builder =
                byteBuddy.subclass(features.mockedType)
                         .name(name)
                         .ignoreAlso(isGroovyMethod())
                         .annotateType(features.stripAnnotations
                             ? new Annotation[0]
                             : features.mockedType.getAnnotations())
                         .implement(new ArrayList<Type>(features.interfaces))
                         .method(matcher)
                           .intercept(dispatcher)
                           .transform(withModifiers(SynchronizationState.PLAIN))
                           .attribute(features.stripAnnotations
                               ? MethodAttributeAppender.NoOp.INSTANCE
                               : INCLUDING_RECEIVER)
                         .method(isHashCode())
                           .intercept(hashCode)
                         .method(isEquals())
                           .intercept(equals)
                         .serialVersionUid(42L)
                         .defineField("mockitoInterceptor", MockMethodInterceptor.class, PRIVATE)
                         .implement(MockAccess.class)
                           .intercept(FieldAccessor.ofBeanProperty());
        if (features.serializableMode == SerializableMode.ACROSS_CLASSLOADERS) {
            builder = builder.implement(CrossClassLoaderSerializableMock.class)
                             .intercept(writeReplace);
        }
        if (readReplace != null) {
            builder = builder.defineMethod("readObject", void.class, Visibility.PRIVATE)
                    .withParameters(ObjectInputStream.class)
                    .throwing(ClassNotFoundException.class, IOException.class)
                    .intercept(readReplace);
        }
        if (name.startsWith(CODEGEN_PACKAGE)) {
            builder = builder.ignoreAlso(isPackagePrivate()
                .or(returns(isPackagePrivate()))
                .or(hasParameters(whereAny(hasType(isPackagePrivate())))));
        }
        return builder.make()
                      .load(classLoader, loader.resolveStrategy(features.mockedType, classLoader, name.startsWith(CODEGEN_PACKAGE)))
                      .getLoaded();
    }

    private Class<?> loadHookType(ClassLoader classLoader) {
        return byteBuddy.subclass(Object.class)
            .make()
            .load(classLoader, loader.resolveStrategy(Object.class, classLoader, true))
            .getLoaded();
    }

    private void loadModuleProble(Class<?> type, Class<?> target, boolean addRead, boolean addOpen, boolean addExports) {
        Implementation.Composable implementation = StubMethod.INSTANCE;
        if (addRead && !canRead(type, target)) {
            implementation = implementation.andThen(MethodCall.invoke(ADD_READ)
                .onMethodCall(MethodCall.invoke(GET_MODULE).on(type))
                .withMethodCall(MethodCall.invoke(GET_MODULE).on(target)));
        }
        if (addOpen && !isOpenedTo(type, target)) {
            implementation = implementation.andThen(MethodCall.invoke(ADD_EXPORTS)
                .onMethodCall(MethodCall.invoke(GET_MODULE).on(type))
                .withMethodCall(MethodCall.invoke(GET_MODULE).on(target)));
        }
        if (addExports && !isExportedTo(type, target)) {
            implementation = implementation.andThen(MethodCall.invoke(ADD_EXPORTS)
                .onMethodCall(MethodCall.invoke(GET_MODULE).on(type))
                .withMethodCall(MethodCall.invoke(GET_MODULE).on(target)));
        }
        if (implementation == StubMethod.INSTANCE) {
            return;
        }
        try {
            byteBuddy.subclass(Object.class)
                    .name(String.format("%s$%s$%d", type.getName(), "MockitoModuleProbe", Math.abs(random.nextInt())))
                    .defineMethod("init", void.class, Visibility.PUBLIC, Ownership.STATIC).intercept(implementation)
                    .make()
                    .load(type.getClassLoader(), loader.resolveStrategy(type, type.getClassLoader(), false))
                    .getLoaded()
                    .getMethod("init")
                    .invoke(null);
        } catch (Exception e) {
            throw new MockitoException(join("Could not force initialization of " + type,
                "",
                "This is required to adjust the module graph to enable mock creation"), e);
        }
    }

    private static ElementMatcher<MethodDescription> isGroovyMethod() {
        return isDeclaredBy(named("groovy.lang.GroovyObjectSupport"));
    }

    private String nameFor(Class<?> type, Set<Class<?>> interfaces, ClassLoader classLoader) {
        boolean relocate;
        if (classLoader instanceof MultipleParentClassLoader) {
            relocate = isComingFromJDK(type) || isComingFromSignedJar(type) || isComingFromSealedPackage(type);
        } else if (loader.isDisrespectingOpenness()) {
            relocate = isComingFromJDK(type);
        } else {
            relocate = isComingFromJDK(type) || isOpenedTo(type, Mockito.class);
            Iterator<Class<?>> it = interfaces.iterator();
            while (!relocate && it.hasNext()) {
                relocate = isOpenedTo(it.next(), Mockito.class);
            }
        }
        String typeName = relocate ? CODEGEN_PACKAGE + type.getSimpleName() : type.getName();
        return String.format("%s$%s$%d", typeName, "MockitoMock", Math.abs(random.nextInt()));
    }

    private boolean isComingFromJDK(Class<?> type) {
        // Comes from the manifest entry :
        // Implementation-Title: Java Runtime Environment
        // This entry is not necessarily present in every jar of the JDK
        return type.getPackage() != null && "Java Runtime Environment".equalsIgnoreCase(type.getPackage().getImplementationTitle())
                || type.getName().startsWith("java.")
                || type.getName().startsWith("javax.");
    }

    private boolean isComingFromSealedPackage(Class<?> type) {
        return type.getPackage() != null && type.getPackage().isSealed();
    }

    private Object getModule(Class<?> type) {
        if (MOCKITO_MODULE == null) {
            return null;
        } else {
            try {
                return GET_MODULE.invoke(type);
            } catch (Exception e) {
                throw new MockitoException("Cannot extract module of " + type, e);
            }
        }
    }

    private boolean isOpenedTo(Class<?> type, Class<?> target) {
        if (type.getPackage() == null) {
            return true;
        }
        try {
            return MOCKITO_MODULE == null || (Boolean) IS_OPEN.invoke(GET_MODULE.invoke(type), type.getPackage().getName(), GET_MODULE.invoke(target));
        } catch (Exception e) {
            throw new MockitoException(join("Cannot assert if " + type + " is opened to Mockito",
                "",
                "Error during reflective access of expected methods of the Java module system"
            ), e);
        }
    }

    private boolean canRead(Class<?> type, Class<?> target) {
        if (type.getPackage() == null) {
            return true;
        }
        try {
            return MOCKITO_MODULE == null || (Boolean) CAN_READ.invoke(GET_MODULE.invoke(type), GET_MODULE.invoke(target));
        } catch (Exception e) {
            throw new MockitoException(join("Cannot assert if " + type + " is opened to Mockito",
                "",
                "Error during reflective access of expected methods of the Java module system"
            ), e);
        }
    }

    private boolean isExportedTo(Class<?> type, Class<?> target) {
        if (type.getPackage() == null) {
            return true;
        }
        try {
            return MOCKITO_MODULE == null || (Boolean) IS_EXPORTED.invoke(GET_MODULE.invoke(type), type.getPackage().getName(), GET_MODULE.invoke(target));
        } catch (Exception e) {
            throw new MockitoException(join("Cannot assert if " + type + " is opened to Mockito",
                "",
                "Error during reflective access of expected methods of the Java module system"
            ), e);
        }
    }

    private boolean isComingFromSignedJar(Class<?> type) {
        return type.getSigners() != null;
    }

    private static void assertVisibility(Class<?> type) {
        if (!Modifier.isPublic(type.getModifiers())) {
            throw new MockitoException(join("Cannot create mock for " + type,
                "",
                "The type is not public and its mock class is loaded by a different class loader.",
                "This can have multiple reasons:",
                " - You are mocking a class with additional interfaces of another class loader",
                " - Mockito is loaded by a different class loader than the mocked type (e.g. with OSGi)",
                " - The thread's context class loader is different than the mock's class loader"));
        }
    }

    private void assertModuleAccessability(Class<?> type) {
        if (!isOpenedTo(type, Mockito.class) && !isExportedTo(type, Mockito.class) && !loader.isDisrespectingOpenness()) {
            throw new MockitoException(join("Mocking target " + type + " is neither opened nor exported to Mockito",
                "",
                "Types that are neither open or exported to Mockito cannot be mocked as mock type cannot be defined within the types module or outside",
                "To allow for mocking this type, you must open the module " + getModule(type)) + " to " + MOCKITO_MODULE);
        }
    }
}
