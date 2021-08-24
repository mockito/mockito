/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static java.lang.Thread.currentThread;
import static net.bytebuddy.description.modifier.Visibility.PRIVATE;
import static net.bytebuddy.dynamic.Transformer.ForMethod.withModifiers;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.implementation.attribute.MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.hasParameters;
import static net.bytebuddy.matcher.ElementMatchers.hasType;
import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.isEquals;
import static net.bytebuddy.matcher.ElementMatchers.isHashCode;
import static net.bytebuddy.matcher.ElementMatchers.isPackagePrivate;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.returns;
import static net.bytebuddy.matcher.ElementMatchers.whereAny;
import static org.mockito.internal.util.StringUtil.join;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.SynchronizationState;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.MultipleParentClassLoader;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import org.mockito.codegen.InjectionBase;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport.CrossClassLoaderSerializableMock;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor.DispatcherDefaultingToRealMethod;
import org.mockito.mock.SerializableMode;

class SubclassBytecodeGenerator implements BytecodeGenerator {

    private static final String CODEGEN_PACKAGE = "org.mockito.codegen.";

    private final SubclassLoader loader;
    private final ModuleHandler handler;
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

    public SubclassBytecodeGenerator(
            Implementation readReplace, ElementMatcher<? super MethodDescription> matcher) {
        this(new SubclassInjectionLoader(), readReplace, matcher);
    }

    protected SubclassBytecodeGenerator(
            SubclassLoader loader,
            Implementation readReplace,
            ElementMatcher<? super MethodDescription> matcher) {
        this.loader = loader;
        this.readReplace = readReplace;
        this.matcher = matcher;
        byteBuddy = new ByteBuddy().with(TypeValidation.DISABLED);
        random = new Random();
        handler = ModuleHandler.make(byteBuddy, loader, random);
    }

    private static boolean needsSamePackageClassLoader(MockFeatures<?> features) {
        if (!Modifier.isPublic(features.mockedType.getModifiers())
                || !features.mockedType.isInterface()) {
            // The mocked type is package private or is not an interface and thus may contain
            // package private methods.
            return true;
        }
        if (hasNonPublicTypeReference(features.mockedType)) {
            return true;
        }

        for (Class<?> iface : features.interfaces) {
            if (!Modifier.isPublic(iface.getModifiers())) {
                return true;
            }
            if (hasNonPublicTypeReference(iface)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasNonPublicTypeReference(Class<?> iface) {
        for (Method method : iface.getMethods()) {
            if (!Modifier.isPublic(method.getReturnType().getModifiers())) {
                return true;
            }
            for (Class<?> param : method.getParameterTypes()) {
                if (!Modifier.isPublic(param.getModifiers())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public <T> Class<? extends T> mockClass(MockFeatures<T> features) {
        MultipleParentClassLoader.Builder loaderBuilder =
                new MultipleParentClassLoader.Builder()
                        .appendMostSpecific(features.mockedType)
                        .appendMostSpecific(features.interfaces)
                        .appendMostSpecific(
                                MockAccess.class, DispatcherDefaultingToRealMethod.class)
                        .appendMostSpecific(
                                MockMethodInterceptor.class,
                                MockMethodInterceptor.ForHashCode.class,
                                MockMethodInterceptor.ForEquals.class);
        ClassLoader contextLoader = currentThread().getContextClassLoader();
        boolean shouldIncludeContextLoader = true;
        if (needsSamePackageClassLoader(features)) {
            // For the generated class to access package-private methods, it must be defined by the
            // same classloader as its type. All the other added classloaders are required to load
            // the type; if the context classloader is a child of the mocked type's defining
            // classloader, it will break a mock that would have worked. Check if the context class
            // loader is a child of the classloader we'd otherwise use, and possibly skip it.
            ClassLoader candidateLoader = loaderBuilder.build();
            for (ClassLoader parent = contextLoader; parent != null; parent = parent.getParent()) {
                if (parent == candidateLoader) {
                    shouldIncludeContextLoader = false;
                    break;
                }
            }
        }
        if (shouldIncludeContextLoader) {
            loaderBuilder = loaderBuilder.appendMostSpecific(contextLoader);
        }
        ClassLoader classLoader = loaderBuilder.build();

        // If Mockito does not need to create a new class loader and if a mock is not based on a JDK
        // type, we attempt
        // to define the mock class in the user runtime package to allow for mocking package private
        // types and methods.
        // This also requires that we are able to access the package of the mocked class either by
        // override or explicit
        // privilege given by the target package being opened to Mockito.
        boolean localMock =
                classLoader == features.mockedType.getClassLoader()
                        && features.serializableMode != SerializableMode.ACROSS_CLASSLOADERS
                        && !isComingFromJDK(features.mockedType)
                        && (loader.isDisrespectingOpenness()
                                || handler.isOpened(features.mockedType, MockAccess.class));
        String typeName;
        if (localMock
                || (loader instanceof MultipleParentClassLoader
                        && !isComingFromJDK(features.mockedType))) {
            typeName = features.mockedType.getName();
        } else {
            typeName =
                    InjectionBase.class.getPackage().getName()
                            + "."
                            + features.mockedType.getSimpleName();
        }
        String name =
                String.format("%s$%s$%d", typeName, "MockitoMock", Math.abs(random.nextInt()));

        if (localMock) {
            handler.adjustModuleGraph(features.mockedType, MockAccess.class, false, true);
            for (Class<?> iFace : features.interfaces) {
                handler.adjustModuleGraph(iFace, features.mockedType, true, false);
                handler.adjustModuleGraph(features.mockedType, iFace, false, true);
            }
        } else {
            boolean exported = handler.isExported(features.mockedType);
            Iterator<Class<?>> it = features.interfaces.iterator();
            while (exported && it.hasNext()) {
                exported = handler.isExported(it.next());
            }
            // We check if all mocked types are exported without qualification to avoid generating a
            // hook type.
            // unless this is necessary. We expect this to be the case for most mocked types what
            // makes this a
            // worthy performance optimization.
            if (exported) {
                assertVisibility(features.mockedType);
                for (Class<?> iFace : features.interfaces) {
                    assertVisibility(iFace);
                }
            } else {
                Class<?> hook = handler.injectionBase(classLoader, typeName);
                assertVisibility(features.mockedType);
                handler.adjustModuleGraph(features.mockedType, hook, true, false);
                for (Class<?> iFace : features.interfaces) {
                    assertVisibility(iFace);
                    handler.adjustModuleGraph(iFace, hook, true, false);
                }
            }
        }

        DynamicType.Builder<T> builder =
                byteBuddy
                        .subclass(features.mockedType)
                        .name(name)
                        .ignoreAlso(isGroovyMethod())
                        .annotateType(
                                features.stripAnnotations
                                        ? new Annotation[0]
                                        : features.mockedType.getAnnotations())
                        .implement(new ArrayList<Type>(features.interfaces))
                        .method(matcher)
                        .intercept(dispatcher)
                        .transform(withModifiers(SynchronizationState.PLAIN))
                        .attribute(
                                features.stripAnnotations
                                        ? MethodAttributeAppender.NoOp.INSTANCE
                                        : INCLUDING_RECEIVER)
                        .serialVersionUid(42L)
                        .defineField("mockitoInterceptor", MockMethodInterceptor.class, PRIVATE)
                        .implement(MockAccess.class)
                        .intercept(FieldAccessor.ofBeanProperty())
                        .method(isHashCode())
                        .intercept(hashCode)
                        .method(isEquals())
                        .intercept(equals);
        if (features.serializableMode == SerializableMode.ACROSS_CLASSLOADERS) {
            builder =
                    builder.implement(CrossClassLoaderSerializableMock.class)
                            .intercept(writeReplace);
        }
        if (readReplace != null) {
            builder =
                    builder.defineMethod("readObject", void.class, Visibility.PRIVATE)
                            .withParameters(ObjectInputStream.class)
                            .throwing(ClassNotFoundException.class, IOException.class)
                            .intercept(readReplace);
        }
        if (name.startsWith(CODEGEN_PACKAGE) || classLoader instanceof MultipleParentClassLoader) {
            builder =
                    builder.ignoreAlso(
                            isPackagePrivate()
                                    .or(returns(isPackagePrivate()))
                                    .or(hasParameters(whereAny(hasType(isPackagePrivate())))));
        }
        return builder.make()
                .load(
                        classLoader,
                        loader.resolveStrategy(features.mockedType, classLoader, localMock))
                .getLoaded();
    }

    @Override
    public void mockClassStatic(Class<?> type) {
        throw new MockitoException("The subclass byte code generator cannot create static mocks");
    }

    @Override
    public void mockClassConstruction(Class<?> type) {
        throw new MockitoException(
                "The subclass byte code generator cannot create construction mocks");
    }

    private <T> Collection<Class<? super T>> getAllTypes(Class<T> type) {
        Collection<Class<? super T>> supertypes = new LinkedList<>();
        supertypes.add(type);
        Class<? super T> superType = type;
        while (superType != null) {
            supertypes.add(superType);
            superType = superType.getSuperclass();
        }
        return supertypes;
    }

    private static ElementMatcher<MethodDescription> isGroovyMethod() {
        return isDeclaredBy(named("groovy.lang.GroovyObjectSupport"))
                .or(isAnnotatedWith(named("groovy.transform.Internal")));
    }

    private boolean isComingFromJDK(Class<?> type) {
        // Comes from the manifest entry :
        // Implementation-Title: Java Runtime Environment
        // This entry is not necessarily present in every jar of the JDK
        return (type.getPackage() != null
                        && "Java Runtime Environment"
                                .equalsIgnoreCase(type.getPackage().getImplementationTitle()))
                || type.getName().startsWith("java.")
                || type.getName().startsWith("javax.");
    }

    private static void assertVisibility(Class<?> type) {
        if (!Modifier.isPublic(type.getModifiers())) {
            throw new MockitoException(
                    join(
                            "Cannot create mock for " + type,
                            "",
                            "The type is not public and its mock class is loaded by a different class loader.",
                            "This can have multiple reasons:",
                            " - You are mocking a class with additional interfaces of another class loader",
                            " - Mockito is loaded by a different class loader than the mocked type (e.g. with OSGi)",
                            " - The thread's context class loader is different than the mock's class loader"));
        }
    }
}
