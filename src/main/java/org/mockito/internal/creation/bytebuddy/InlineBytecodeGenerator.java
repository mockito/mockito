/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.utility.RandomString;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.internal.util.concurrent.WeakConcurrentSet;
import org.mockito.mock.SerializableMode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFixedValue.OfConstant.of;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class InlineBytecodeGenerator implements BytecodeGenerator, ClassFileTransformer {

    @SuppressWarnings("unchecked")
    static final Set<Class<?>> EXCLUDES = new HashSet<Class<?>>(Arrays.asList(Class.class,
            Boolean.class,
            Byte.class,
            Short.class,
            Character.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            String.class));

    private final Instrumentation instrumentation;

    private final ByteBuddy byteBuddy;

    private final WeakConcurrentSet<Class<?>> mocked;

    private final String identifier;

    private final MockMethodAdvice advice;

    private final BytecodeGenerator subclassEngine;

    public InlineBytecodeGenerator(Instrumentation instrumentation, WeakConcurrentMap<Object, MockMethodInterceptor> mocks) {
        this.instrumentation = instrumentation;
        byteBuddy = new ByteBuddy()
                .with(TypeValidation.DISABLED)
                .with(Implementation.Context.Disabled.Factory.INSTANCE);
        mocked = new WeakConcurrentSet<Class<?>>(WeakConcurrentSet.Cleaner.INLINE);
        identifier = RandomString.make();
        advice = new MockMethodAdvice(mocks, identifier);
        subclassEngine = new TypeCachingBytecodeGenerator(new SubclassBytecodeGenerator(to(MockMethodAdvice.ForReadObject.class)
                .appendParameterBinder(of(MockMethodAdvice.Identifier.class, identifier)), isAbstract().or(isNative())), false);
        MockMethodDispatcher.set(identifier, advice);
        instrumentation.addTransformer(this, true);
    }

    @Override
    public <T> Class<? extends T> mockClass(MockFeatures<T> features) {
        boolean subclassingRequired = !features.interfaces.isEmpty()
                || features.serializableMode != SerializableMode.NONE
                || Modifier.isAbstract(features.mockedType.getModifiers());

        checkSupportedCombination(subclassingRequired, features);

        synchronized (this) {
            triggerRetransformation(features);
        }

        return subclassingRequired ?
                subclassEngine.mockClass(features) :
                features.mockedType;
    }

    private <T> void triggerRetransformation(MockFeatures<T> features) {
        Set<Class<?>> types = new HashSet<Class<?>>();
        Class<?> type = features.mockedType;
        do {
            if (mocked.add(type)) {
                types.add(type);
                addInterfaces(types, type.getInterfaces());
            }
            type = type.getSuperclass();
        } while (type != null);
        if (!types.isEmpty()) {
            try {
                instrumentation.retransformClasses(types.toArray(new Class<?>[types.size()]));
            } catch (UnmodifiableClassException exception) {
                for (Class<?> failed : types) {
                    mocked.remove(failed);
                }
                throw new MockitoException("Could not modify all classes " + types, exception);
            }
        }
    }

    private <T> void checkSupportedCombination(boolean subclassingRequired, MockFeatures<T> features) {
        if (subclassingRequired
                && !features.mockedType.isArray()
                && !features.mockedType.isPrimitive()
                && Modifier.isFinal(features.mockedType.getModifiers())) {
            throw new MockitoException("Unsupported settings with this type '" + features.mockedType.getName() + "'");
        }
    }

    private void addInterfaces(Set<Class<?>> types, Class<?>[] interfaces) {
        for (Class<?> type : interfaces) {
            if (mocked.add(type)) {
                types.add(type);
                addInterfaces(types, type.getInterfaces());
            }
        }
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (classBeingRedefined == null
                || !mocked.contains(classBeingRedefined)
                || EXCLUDES.contains(classBeingRedefined)) {
            return null;
        } else {
            try {
                // Note: The VM seems to erase parameter names from the provided class file. We therefore first attempt to read the
                // class file from the class loader. This might revert transformations applied by previous agents.
                return byteBuddy.redefine(classBeingRedefined, new ClassFileLocator.Compound(ClassFileLocator.ForClassLoader.of(loader),
                        ClassFileLocator.Simple.of(classBeingRedefined.getName(), classfileBuffer)))
                        .visit(Advice.withCustomMapping()
                                .bind(MockMethodAdvice.Identifier.class, identifier)
                                .to(MockMethodAdvice.class).on(isVirtual()
                                        .and(not(isBridge().or(isHashCode()).or(isEquals()).or(isDefaultFinalizer())))
                                        .and(not(isDeclaredBy(nameStartsWith("java.")).<MethodDescription>and(isPackagePrivate())))))
                        .visit(Advice.withCustomMapping()
                                .bind(MockMethodAdvice.Identifier.class, identifier)
                                .to(MockMethodAdvice.ForHashCode.class).on(isHashCode()))
                        .visit(Advice.withCustomMapping()
                                .bind(MockMethodAdvice.Identifier.class, identifier)
                                .to(MockMethodAdvice.ForEquals.class).on(isEquals()))
                        .make()
                        .getBytes();
            } catch (Throwable throwable) {
                return null;
            }
        }
    }
}
