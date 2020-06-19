/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static net.bytebuddy.implementation.MethodDelegation.withDefaultConfiguration;
import static net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder.ParameterBinder.ForFixedValue.OfConstant.of;
import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.mockito.internal.util.StringUtil.join;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.*;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;
import net.bytebuddy.utility.RandomString;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.internal.util.concurrent.WeakConcurrentSet;
import org.mockito.mock.SerializableMode;

public class InlineBytecodeGenerator implements BytecodeGenerator, ClassFileTransformer {

    private static final String PRELOAD = "org.mockito.inline.preload";

    @SuppressWarnings("unchecked")
    static final Set<Class<?>> EXCLUDES =
            new HashSet<Class<?>>(
                    Arrays.asList(
                            Class.class,
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
    private final WeakConcurrentSet<Class<?>> mocked, flatMocked;
    private final BytecodeGenerator subclassEngine;
    private final AsmVisitorWrapper mockTransformer;

    private final Method getModule, canRead, redefineModule;

    private volatile Throwable lastException;

    public InlineBytecodeGenerator(
            Instrumentation instrumentation,
            WeakConcurrentMap<Object, MockMethodInterceptor> mocks,
            DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics) {
        preload();
        this.instrumentation = instrumentation;
        byteBuddy =
                new ByteBuddy()
                        .with(TypeValidation.DISABLED)
                        .with(Implementation.Context.Disabled.Factory.INSTANCE)
                        .with(MethodGraph.Compiler.ForDeclaredMethods.INSTANCE);
        mocked = new WeakConcurrentSet<>(WeakConcurrentSet.Cleaner.INLINE);
        flatMocked = new WeakConcurrentSet<>(WeakConcurrentSet.Cleaner.INLINE);
        String identifier = RandomString.make();
        subclassEngine =
                new TypeCachingBytecodeGenerator(
                        new SubclassBytecodeGenerator(
                                withDefaultConfiguration()
                                        .withBinders(
                                                of(MockMethodAdvice.Identifier.class, identifier))
                                        .to(MockMethodAdvice.ForReadObject.class),
                                isAbstract().or(isNative()).or(isToString())),
                        false);
        mockTransformer =
                new AsmVisitorWrapper.ForDeclaredMethods()
                        .method(
                                isVirtual()
                                        .and(
                                                not(
                                                        isBridge()
                                                                .or(isHashCode())
                                                                .or(isEquals())
                                                                .or(isDefaultFinalizer())))
                                        .and(
                                                not(
                                                        isDeclaredBy(nameStartsWith("java."))
                                                                .<MethodDescription>and(
                                                                        isPackagePrivate()))),
                                Advice.withCustomMapping()
                                        .bind(MockMethodAdvice.Identifier.class, identifier)
                                        .to(MockMethodAdvice.class))
                        .method(
                                isStatic(),
                                Advice.withCustomMapping()
                                        .bind(MockMethodAdvice.Identifier.class, identifier)
                                        .to(MockMethodAdvice.ForStatic.class))
                        .method(
                                isHashCode(),
                                Advice.withCustomMapping()
                                        .bind(MockMethodAdvice.Identifier.class, identifier)
                                        .to(MockMethodAdvice.ForHashCode.class))
                        .method(
                                isEquals(),
                                Advice.withCustomMapping()
                                        .bind(MockMethodAdvice.Identifier.class, identifier)
                                        .to(MockMethodAdvice.ForEquals.class));
        Method getModule, canRead, redefineModule;
        try {
            getModule = Class.class.getMethod("getModule");
            canRead = getModule.getReturnType().getMethod("canRead", getModule.getReturnType());
            redefineModule =
                    Instrumentation.class.getMethod(
                            "redefineModule",
                            getModule.getReturnType(),
                            Set.class,
                            Map.class,
                            Map.class,
                            Set.class,
                            Map.class);
        } catch (Exception ignored) {
            getModule = null;
            canRead = null;
            redefineModule = null;
        }
        this.getModule = getModule;
        this.canRead = canRead;
        this.redefineModule = redefineModule;
        MockMethodDispatcher.set(
                identifier, new MockMethodAdvice(mocks, mockedStatics, identifier));
        instrumentation.addTransformer(this, true);
    }

    /**
     * Mockito allows to mock about any type, including such types that we are relying on ourselves. This can cause a circularity:
     * In order to check if an instance is a mock we need to look up if this instance is registered in the {@code mocked} set. But to look
     * up this instance, we need to create key instances that rely on weak reference properties. Loading the later classes will happen before
     * the key instances are completed what will cause Mockito to check if those key instances are themselves mocks what causes a loop which
     * results in a circularity error. This is not normally a problem as we explicitly check if the instance that we investigate is one of
     * our instance of which we hold a reference by reference equality what does not cause any code execution. But it seems like the load
     * order plays a role here with unloaded types being loaded before we even get to check the mock instance property. To avoid this, we are
     * making sure that crucuial JVM types are loaded before we create the first inline mock. Unfortunately, these types dependant on a JVM's
     * implementation and we can only maintain types that we know of from well-known JVM implementations such as HotSpot and extend this list
     * once we learn of further problematic types for future Java versions. To allow users to whitelist their own types, we do not also offer
     * a property that allows running problematic tests before a new Mockito version can be released and that allows us to ask users to
     * easily validate that whitelisting actually solves a problem as circularities could also be caused by other problems.
     */
    private static void preload() {
        String preloads = System.getProperty(PRELOAD);
        if (preloads == null) {
            preloads =
                    "java.lang.WeakPairMap,java.lang.WeakPairMap$Pair,java.lang.WeakPairMap$Pair$Weak";
        }
        for (String preload : preloads.split(",")) {
            try {
                Class.forName(preload, false, null);
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    @Override
    public <T> Class<? extends T> mockClass(MockFeatures<T> features) {
        boolean subclassingRequired =
                !features.interfaces.isEmpty()
                        || features.serializableMode != SerializableMode.NONE
                        || Modifier.isAbstract(features.mockedType.getModifiers());

        checkSupportedCombination(subclassingRequired, features);

        Set<Class<?>> types = new HashSet<>();
        types.add(features.mockedType);
        types.addAll(features.interfaces);
        synchronized (this) {
            triggerRetransformation(types, false);
        }

        return subclassingRequired ? subclassEngine.mockClass(features) : features.mockedType;
    }

    @Override
    public void mockClassStatic(Class<?> type) {
        triggerRetransformation(Collections.singleton(type), true);
    }

    private <T> void triggerRetransformation(Set<Class<?>> types, boolean flat) {
        Set<Class<?>> targets = new HashSet<Class<?>>();

        for (Class<?> type : types) {
            if (flat) {
                if (!mocked.contains(type) && flatMocked.add(type)) {
                    targets.add(type);
                }
            } else {
                do {
                    if (mocked.add(type)) {
                        if (!flatMocked.remove(type)) {
                            targets.add(type);
                        }
                        addInterfaces(targets, type.getInterfaces());
                    }
                    type = type.getSuperclass();
                } while (type != null);
            }
        }

        if (!targets.isEmpty()) {
            try {
                assureCanReadMockito(targets);
                instrumentation.retransformClasses(targets.toArray(new Class<?>[targets.size()]));
                Throwable throwable = lastException;
                if (throwable != null) {
                    throw new IllegalStateException(
                            join(
                                    "Byte Buddy could not instrument all classes within the mock's type hierarchy",
                                    "",
                                    "This problem should never occur for javac-compiled classes. This problem has been observed for classes that are:",
                                    " - Compiled by older versions of scalac",
                                    " - Classes that are part of the Android distribution"),
                            throwable);
                }
            } catch (Exception exception) {
                for (Class<?> failed : targets) {
                    mocked.remove(failed);
                    flatMocked.remove(failed);
                }
                throw new MockitoException("Could not modify all classes " + targets, exception);
            } finally {
                lastException = null;
            }
        }
    }

    private void assureCanReadMockito(Set<Class<?>> types) {
        if (redefineModule == null) {
            return;
        }
        Set<Object> modules = new HashSet<Object>();
        try {
            Object target =
                    getModule.invoke(
                            Class.forName(
                                    "org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher",
                                    false,
                                    null));
            for (Class<?> type : types) {
                Object module = getModule.invoke(type);
                if (!modules.contains(module) && !(Boolean) canRead.invoke(module, target)) {
                    modules.add(module);
                }
            }
            for (Object module : modules) {
                redefineModule.invoke(
                        instrumentation,
                        module,
                        Collections.singleton(target),
                        Collections.emptyMap(),
                        Collections.emptyMap(),
                        Collections.emptySet(),
                        Collections.emptyMap());
            }
        } catch (Exception e) {
            throw new IllegalStateException(
                    join(
                            "Could not adjust module graph to make the mock instance dispatcher visible to some classes",
                            "",
                            "At least one of those modules: "
                                    + modules
                                    + " is not reading the unnamed module of the bootstrap loader",
                            "Without such a read edge, the classes that are redefined to become mocks cannot access the mock dispatcher.",
                            "To circumvent this, Mockito attempted to add a read edge to this module what failed for an unexpected reason"),
                    e);
        }
    }

    private <T> void checkSupportedCombination(
            boolean subclassingRequired, MockFeatures<T> features) {
        if (subclassingRequired
                && !features.mockedType.isArray()
                && !features.mockedType.isPrimitive()
                && Modifier.isFinal(features.mockedType.getModifiers())) {
            throw new MockitoException(
                    "Unsupported settings with this type '" + features.mockedType.getName() + "'");
        }
    }

    private void addInterfaces(Set<Class<?>> types, Class<?>[] interfaces) {
        for (Class<?> type : interfaces) {
            if (mocked.add(type)) {
                if (!flatMocked.remove(type)) {
                    types.add(type);
                }
                addInterfaces(types, type.getInterfaces());
            }
        }
    }

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) {
        if (classBeingRedefined == null
                || !mocked.contains(classBeingRedefined)
                        && !flatMocked.contains(classBeingRedefined)
                || EXCLUDES.contains(classBeingRedefined)) {
            return null;
        } else {
            try {
                return byteBuddy
                        .redefine(
                                classBeingRedefined,
                                ClassFileLocator.Simple.of(
                                        classBeingRedefined.getName(), classfileBuffer))
                        // Note: The VM erases parameter meta data from the provided class file
                        // (bug). We just add this information manually.
                        .visit(new ParameterWritingVisitorWrapper(classBeingRedefined))
                        .visit(mockTransformer)
                        .make()
                        .getBytes();
            } catch (Throwable throwable) {
                lastException = throwable;
                return null;
            }
        }
    }

    private static class ParameterWritingVisitorWrapper extends AsmVisitorWrapper.AbstractBase {

        private final Class<?> type;

        private ParameterWritingVisitorWrapper(Class<?> type) {
            this.type = type;
        }

        @Override
        public ClassVisitor wrap(
                TypeDescription instrumentedType,
                ClassVisitor classVisitor,
                Implementation.Context implementationContext,
                TypePool typePool,
                FieldList<FieldDescription.InDefinedShape> fields,
                MethodList<?> methods,
                int writerFlags,
                int readerFlags) {
            return implementationContext.getClassFileVersion().isAtLeast(ClassFileVersion.JAVA_V8)
                    ? new ParameterAddingClassVisitor(
                            classVisitor, new TypeDescription.ForLoadedType(type))
                    : classVisitor;
        }

        private static class ParameterAddingClassVisitor extends ClassVisitor {

            private final TypeDescription typeDescription;

            private ParameterAddingClassVisitor(ClassVisitor cv, TypeDescription typeDescription) {
                super(OpenedClassReader.ASM_API, cv);
                this.typeDescription = typeDescription;
            }

            @Override
            public MethodVisitor visitMethod(
                    int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor =
                        super.visitMethod(access, name, desc, signature, exceptions);
                MethodList<?> methodList =
                        typeDescription
                                .getDeclaredMethods()
                                .filter(
                                        (name.equals(MethodDescription.CONSTRUCTOR_INTERNAL_NAME)
                                                        ? isConstructor()
                                                        : ElementMatchers.<MethodDescription>named(
                                                                name))
                                                .and(hasDescriptor(desc)));
                if (methodList.size() == 1
                        && methodList.getOnly().getParameters().hasExplicitMetaData()) {
                    for (ParameterDescription parameterDescription :
                            methodList.getOnly().getParameters()) {
                        methodVisitor.visitParameter(
                                parameterDescription.getName(),
                                parameterDescription.getModifiers());
                    }
                    return new MethodParameterStrippingMethodVisitor(methodVisitor);
                } else {
                    return methodVisitor;
                }
            }
        }

        private static class MethodParameterStrippingMethodVisitor extends MethodVisitor {

            public MethodParameterStrippingMethodVisitor(MethodVisitor mv) {
                super(OpenedClassReader.ASM_API, mv);
            }

            @Override
            public void visitParameter(String name, int access) {
                // suppress to avoid additional writing of the parameter if retained.
            }
        }
    }
}
