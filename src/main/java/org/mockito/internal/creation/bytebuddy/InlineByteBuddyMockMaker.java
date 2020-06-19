/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.mockito.internal.creation.bytebuddy.InlineBytecodeGenerator.EXCLUDES;
import static org.mockito.internal.util.StringUtil.join;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import javax.tools.ToolProvider;

import net.bytebuddy.agent.ByteBuddyAgent;
import org.mockito.Incubating;
import org.mockito.creation.instance.Instantiator;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.base.MockitoInitializationException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.util.Platform;
import org.mockito.internal.util.concurrent.DetachedThreadLocal;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InlineMockMaker;

/**
 * Agent and subclass based mock maker.
 * <p>
 * This mock maker which uses a combination of the Java instrumentation API and sub-classing rather than creating
 * a new sub-class to create a mock. This way, it becomes possible to mock final types and methods. This mock
 * maker <strong>must to be activated explicitly</strong> for supporting mocking final types and methods:
 * <p>
 * <p>
 * This mock maker can be activated by creating the file <code>/mockito-extensions/org.mockito.plugins.MockMaker</code>
 * containing the text <code>mock-maker-inline</code> or <code>org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker</code>.
 * <p>
 * <p>
 * This mock maker will make a best effort to avoid subclass creation when creating a mock. Otherwise it will use the
 * <code>org.mockito.internal.creation.bytebuddy.SubclassByteBuddyMockMaker</code> to create the mock class. That means
 * that the following condition is true
 * <p>
 * <pre class="code"><code class="java">
 * class Foo { }
 * assert mock(Foo.class).getClass() == Foo.class;
 * </pre></code>
 * <p>
 * unless any of the following conditions is met, in such case the mock maker <em>fall backs</em> to the
 * the creation of a subclass.
 * <p>
 * <ul>
 * <li>the type to mock is an abstract class.</li>
 * <li>the mock is set to require additional interfaces.</li>
 * <li>the mock is <a href="#20">explicitly set to support serialization</a>.</li>
 * </ul>
 * <p>
 * <p>
 * Some type of the JDK cannot be mocked, this includes <code>Class</code>, <code>String</code>, and wrapper types.
 * <p>
 * <p>
 * Nevertheless, final methods of such types are mocked when using the inlining mock maker. Mocking final types and enums
 * does however remain impossible when explicitly requiring serialization support or when adding ancillary interfaces.
 * <p>
 * <p>
 * Important behavioral changes when using inline-mocks:
 * <ul>
 * <li>Mockito is capable of mocking package-private methods even if they are defined in different packages than
 * the mocked type. Mockito voluntarily never mocks package-visible methods within <code>java.*</code> packages.</li>
 * <li>Additionally to final types, Mockito can now mock types that are not visible for extension; such types
 * include private types in a protected package.</li>
 * <li>Mockito can no longer mock <code>native</code> methods. Inline mocks require byte code manipulation of a
 * method where native methods do not offer any byte code to manipulate.</li>
 * <li>Mockito cannot longer strip <code>synchronized</code> modifiers from mocked instances.</li>
 * </ul>
 * <p>
 * <p>
 * Note that inline mocks require a Java agent to be attached. Mockito will attempt an attachment of a Java agent upon
 * loading the mock maker for creating inline mocks. Such runtime attachment is only possible when using a JVM that
 * is part of a JDK or when using a Java 9 VM. When running on a non-JDK VM prior to Java 9, it is however possible to
 * manually add the <a href="http://bytebuddy.net">Byte Buddy Java agent jar</a> using the <code>-javaagent</code>
 * parameter upon starting the JVM. Furthermore, the inlining mock maker requires the VM to support class retransformation
 * (also known as HotSwap). All major VM distributions such as HotSpot (OpenJDK), J9 (IBM/Websphere) or Zing (Azul)
 * support this feature.
 */
@Incubating
public class InlineByteBuddyMockMaker implements ClassCreatingMockMaker, InlineMockMaker {

    private static final Instrumentation INSTRUMENTATION;

    private static final Throwable INITIALIZATION_ERROR;

    static {
        Instrumentation instrumentation;
        Throwable initializationError = null;
        try {
            try {
                instrumentation = ByteBuddyAgent.install();
                if (!instrumentation.isRetransformClassesSupported()) {
                    throw new IllegalStateException(
                            join(
                                    "Byte Buddy requires retransformation for creating inline mocks. This feature is unavailable on the current VM.",
                                    "",
                                    "You cannot use this mock maker on this VM"));
                }
                File boot = File.createTempFile("mockitoboot", ".jar");
                boot.deleteOnExit();
                JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(boot));
                try {
                    String source =
                            "org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher";
                    InputStream inputStream =
                            InlineByteBuddyMockMaker.class
                                    .getClassLoader()
                                    .getResourceAsStream(source + ".raw");
                    if (inputStream == null) {
                        throw new IllegalStateException(
                                join(
                                        "The MockMethodDispatcher class file is not locatable: "
                                                + source
                                                + ".raw",
                                        "",
                                        "The class loader responsible for looking up the resource: "
                                                + InlineByteBuddyMockMaker.class.getClassLoader()));
                    }
                    outputStream.putNextEntry(new JarEntry(source + ".class"));
                    try {
                        int length;
                        byte[] buffer = new byte[1024];
                        while ((length = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, length);
                        }
                    } finally {
                        inputStream.close();
                    }
                    outputStream.closeEntry();
                } finally {
                    outputStream.close();
                }
                try (JarFile jarfile = new JarFile(boot)) {
                    instrumentation.appendToBootstrapClassLoaderSearch(jarfile);
                }
                try {
                    Class.forName(
                            "org.mockito.internal.creation.bytebuddy.inject.MockMethodDispatcher",
                            false,
                            null);
                } catch (ClassNotFoundException cnfe) {
                    throw new IllegalStateException(
                            join(
                                    "Mockito failed to inject the MockMethodDispatcher class into the bootstrap class loader",
                                    "",
                                    "It seems like your current VM does not support the instrumentation API correctly."),
                            cnfe);
                }
            } catch (IOException ioe) {
                throw new IllegalStateException(
                        join(
                                "Mockito could not self-attach a Java agent to the current VM. This feature is required for inline mocking.",
                                "This error occured due to an I/O error during the creation of this agent: "
                                        + ioe,
                                "",
                                "Potentially, the current VM does not support the instrumentation API correctly"),
                        ioe);
            }
        } catch (Throwable throwable) {
            instrumentation = null;
            initializationError = throwable;
        }
        INSTRUMENTATION = instrumentation;
        INITIALIZATION_ERROR = initializationError;
    }

    private final BytecodeGenerator bytecodeGenerator;

    private final WeakConcurrentMap<Object, MockMethodInterceptor> mocks =
            new WeakConcurrentMap.WithInlinedExpunction<Object, MockMethodInterceptor>();

    private final DetachedThreadLocal<Map<Class<?>, MockMethodInterceptor>> mockedStatics =
            new DetachedThreadLocal<>(DetachedThreadLocal.Cleaner.INLINE);

    public InlineByteBuddyMockMaker() {
        if (INITIALIZATION_ERROR != null) {
            throw new MockitoInitializationException(
                    join(
                            "Could not initialize inline Byte Buddy mock maker. (This mock maker is not supported on Android.)",
                            ToolProvider.getSystemJavaCompiler() == null
                                    ? "Are you running a JRE instead of a JDK? The inline mock maker needs to be run on a JDK.\n"
                                    : "",
                            Platform.describe()),
                    INITIALIZATION_ERROR);
        }
        bytecodeGenerator =
                new TypeCachingBytecodeGenerator(
                        new InlineBytecodeGenerator(INSTRUMENTATION, mocks, mockedStatics), true);
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        Class<? extends T> type = createMockType(settings);

        Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(settings);
        try {
            T instance = instantiator.newInstance(type);
            MockMethodInterceptor mockMethodInterceptor =
                    new MockMethodInterceptor(handler, settings);
            mocks.put(instance, mockMethodInterceptor);
            if (instance instanceof MockAccess) {
                ((MockAccess) instance).setMockitoInterceptor(mockMethodInterceptor);
            }
            return instance;
        } catch (org.mockito.creation.instance.InstantiationException e) {
            throw new MockitoException(
                    "Unable to create mock instance of type '" + type.getSimpleName() + "'", e);
        }
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        try {
            return bytecodeGenerator.mockClass(
                    MockFeatures.withMockFeatures(
                            settings.getTypeToMock(),
                            settings.getExtraInterfaces(),
                            settings.getSerializableMode(),
                            settings.isStripAnnotations()));
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(settings, bytecodeGenerationFailed);
        }
    }

    private <T> RuntimeException prettifyFailure(
            MockCreationSettings<T> mockFeatures, Exception generationFailed) {
        if (mockFeatures.getTypeToMock().isArray()) {
            throw new MockitoException(
                    join("Arrays cannot be mocked: " + mockFeatures.getTypeToMock() + ".", ""),
                    generationFailed);
        }
        if (Modifier.isFinal(mockFeatures.getTypeToMock().getModifiers())) {
            throw new MockitoException(
                    join(
                            "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                            "Can not mock final classes with the following settings :",
                            " - explicit serialization (e.g. withSettings().serializable())",
                            " - extra interfaces (e.g. withSettings().extraInterfaces(...))",
                            "",
                            "You are seeing this disclaimer because Mockito is configured to create inlined mocks.",
                            "You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.",
                            "",
                            "Underlying exception : " + generationFailed),
                    generationFailed);
        }
        if (Modifier.isPrivate(mockFeatures.getTypeToMock().getModifiers())) {
            throw new MockitoException(
                    join(
                            "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                            "Most likely it is a private class that is not visible by Mockito",
                            "",
                            "You are seeing this disclaimer because Mockito is configured to create inlined mocks.",
                            "You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.",
                            ""),
                    generationFailed);
        }
        throw new MockitoException(
                join(
                        "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                        "",
                        "If you're not sure why you're getting this error, please report to the mailing list.",
                        "",
                        Platform.warnForVM(
                                "IBM J9 VM",
                                "Early IBM virtual machine are known to have issues with Mockito, please upgrade to an up-to-date version.\n",
                                "Hotspot",
                                Platform.isJava8BelowUpdate45()
                                        ? "Java 8 early builds have bugs that were addressed in Java 1.8.0_45, please update your JDK!\n"
                                        : ""),
                        Platform.describe(),
                        "",
                        "You are seeing this disclaimer because Mockito is configured to create inlined mocks.",
                        "You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.",
                        "",
                        "Underlying exception : " + generationFailed),
                generationFailed);
    }

    @Override
    public MockHandler getHandler(Object mock) {
        MockMethodInterceptor interceptor;
        if (mock instanceof Class<?>) {
            Map<Class<?>, MockMethodInterceptor> interceptors = mockedStatics.get();
            interceptor = interceptors != null ? interceptors.get(mock) : null;
        } else {
            interceptor = mocks.get(mock);
        }
        if (interceptor == null) {
            return null;
        } else {
            return interceptor.handler;
        }
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        MockMethodInterceptor mockMethodInterceptor =
                new MockMethodInterceptor(newHandler, settings);
        if (mock instanceof Class<?>) {
            Map<Class<?>, MockMethodInterceptor> interceptors = mockedStatics.get();
            if (interceptors == null || !interceptors.containsKey(mock)) {
                throw new MockitoException(
                        "Cannot reset "
                                + mock
                                + " which is not currently registered as a static mock");
            }
            interceptors.put((Class<?>) mock, mockMethodInterceptor);
        } else {
            if (!mocks.containsKey(mock)) {
                throw new MockitoException(
                        "Cannot reset " + mock + " which is not currently registered as a mock");
            }
            mocks.put(mock, mockMethodInterceptor);
            if (mock instanceof MockAccess) {
                ((MockAccess) mock).setMockitoInterceptor(mockMethodInterceptor);
            }
        }
    }

    @Override
    public void clearMock(Object mock) {
        if (mock instanceof Class<?>) {
            for (Map<Class<?>, ?> entry : mockedStatics.getBackingMap().target.values()) {
                entry.remove(mock);
            }
        } else {
            mocks.remove(mock);
        }
    }

    @Override
    public void clearAllMocks() {
        mockedStatics.getBackingMap().clear();
        mocks.clear();
    }

    @Override
    public TypeMockability isTypeMockable(final Class<?> type) {
        return new TypeMockability() {
            @Override
            public boolean mockable() {
                return INSTRUMENTATION.isModifiableClass(type) && !EXCLUDES.contains(type);
            }

            @Override
            public String nonMockableReason() {
                if (mockable()) {
                    return "";
                }
                if (type.isPrimitive()) {
                    return "primitive type";
                }
                if (EXCLUDES.contains(type)) {
                    return "Cannot mock wrapper types, String.class or Class.class";
                }
                return "VM does not support modification of given type";
            }
        };
    }

    @Override
    public <T> StaticMockControl<T> createStaticMock(
            Class<T> type, MockCreationSettings<T> settings, MockHandler handler) {
        if (type == ConcurrentHashMap.class) {
            throw new MockitoException(
                    "It is not possible to mock static methods of ConcurrentHashMap "
                            + "to avoid infinitive loops within Mockito's implementation of static mock handling");
        }

        bytecodeGenerator.mockClassStatic(type);

        Map<Class<?>, MockMethodInterceptor> interceptors = mockedStatics.get();
        if (interceptors == null) {
            interceptors = new WeakHashMap<>();
            mockedStatics.set(interceptors);
        }

        return new InlineStaticMockControl<>(type, interceptors, settings, handler);
    }

    private static class InlineStaticMockControl<T> implements StaticMockControl<T> {

        private final Class<T> type;

        private final Map<Class<?>, MockMethodInterceptor> interceptors;

        private final MockCreationSettings<T> settings;
        private final MockHandler handler;

        private InlineStaticMockControl(
                Class<T> type,
                Map<Class<?>, MockMethodInterceptor> interceptors,
                MockCreationSettings<T> settings,
                MockHandler handler) {
            this.type = type;
            this.interceptors = interceptors;
            this.settings = settings;
            this.handler = handler;
        }

        @Override
        public Class<T> getType() {
            return type;
        }

        @Override
        public void enable() {
            if (interceptors.putIfAbsent(type, new MockMethodInterceptor(handler, settings))
                    != null) {
                throw new MockitoException(
                        join(
                                "For "
                                        + type.getName()
                                        + ", static mocking is already registered in the current thread",
                                "",
                                "To create a new mock, the existing static mock registration must be deregistered"));
            }
        }

        @Override
        public void disable() {
            if (interceptors.remove(type) == null) {
                throw new MockitoException(
                        join(
                                "Could not deregister "
                                        + type.getName()
                                        + " as a static mock since it is not currently registered",
                                "",
                                "To register a static mock, use Mockito.mockStatic("
                                        + type.getSimpleName()
                                        + ".class)"));
            }
        }
    }
}
