/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.ClassFileLocator;
import org.mockito.Incubating;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.instance.Instantiator;
import org.mockito.internal.util.Platform;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static org.mockito.internal.creation.bytebuddy.InlineBytecodeGenerator.EXCLUDES;
import static org.mockito.internal.util.StringJoiner.join;

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
public class InlineByteBuddyMockMaker implements ClassCreatingMockMaker {

    private final Instrumentation instrumentation;

    private final BytecodeGenerator bytecodeGenerator;

    private final WeakConcurrentMap<Object, MockMethodInterceptor> mocks = new WeakConcurrentMap.WithInlinedExpunction<Object, MockMethodInterceptor>();

    public InlineByteBuddyMockMaker() {
        try {
            instrumentation = ByteBuddyAgent.install();
            if (!instrumentation.isRetransformClassesSupported()) {
                throw new MockitoException(join(
                        "Byte Buddy requires retransformation for creating inline mocks. This feature is unavailable on the current VM.",
                        "",
                        "You cannot use this mock maker on this VM:",
                        Platform.describe()));
            }
            File boot = File.createTempFile("mockitoboot", "jar");
            boot.deleteOnExit();
            JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(boot));
            try {
                outputStream.putNextEntry(new JarEntry("org/mockito/internal/creation/bytebuddy/MockMethodDispatcher.class"));
                outputStream.write(ClassFileLocator.ForClassLoader.of(InlineByteBuddyMockMaker.class.getClassLoader())
                        .locate("org.mockito.internal.creation.bytebuddy.MockMethodDispatcher")
                        .resolve());
                outputStream.closeEntry();
            } finally {
                outputStream.close();
            }
            instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(boot));
            try {
                Class<?> dispatcher = Class.forName("org.mockito.internal.creation.bytebuddy.MockMethodDispatcher");
                if (dispatcher.getClassLoader() != null) {
                    throw new MockitoException(join(
                            "The MockMethodDispatcher must not be loaded manually but must be injected into the bootstrap class loader.",
                            "",
                            "The dispatcher class was already loaded by: " + dispatcher.getClassLoader()));
                }
            } catch (ClassNotFoundException cnfe) {
                throw new MockitoException(join(
                        "Mockito failed to inject the MockMethodDispatcher class into the bootstrap class loader",
                        "",
                        "It seems like your current VM does not support the instrumentation API correctly:",
                        Platform.describe()), cnfe);
            }
            bytecodeGenerator = new TypeCachingBytecodeGenerator(new InlineBytecodeGenerator(instrumentation, mocks), true);
        } catch (IOException ioe) {
            throw new MockitoException(join(
                    "Mockito could not self-attach a Java agent to the current VM. This feature is required for inline mocking.",
                    "This error occured due to an I/O error during the creation of this agent: " + ioe,
                    "",
                    "Potentially, the current VM does not support the instrumentation API correctly:",
                    Platform.describe()), ioe);
        }
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        Class<? extends T> type = createMockType(settings);

        Instantiator instantiator = Plugins.getInstantiatorProvider().getInstantiator(settings);
        try {
            T instance = instantiator.newInstance(type);
            MockMethodInterceptor mockMethodInterceptor = new MockMethodInterceptor(asInternalMockHandler(handler), settings);
            mocks.put(instance, mockMethodInterceptor);
            if (instance instanceof MockAccess) {
                ((MockAccess) instance).setMockitoInterceptor(mockMethodInterceptor);
            }
            return instance;
        } catch (org.mockito.internal.creation.instance.InstantiationException e) {
            throw new MockitoException("Unable to create mock instance of type '" + type.getSimpleName() + "'", e);
        }
    }

    @Override
    public <T> Class<? extends T> createMockType(MockCreationSettings<T> settings) {
        try {
            return bytecodeGenerator.mockClass(MockFeatures.withMockFeatures(
                    settings.getTypeToMock(),
                    settings.getExtraInterfaces(),
                    settings.getSerializableMode()
            ));
        } catch (Exception bytecodeGenerationFailed) {
            throw prettifyFailure(settings, bytecodeGenerationFailed);
        }
    }

    private <T> RuntimeException prettifyFailure(MockCreationSettings<T> mockFeatures, Exception generationFailed) {
        if (mockFeatures.getTypeToMock().isArray()) {
            throw new MockitoException(join(
                    "Arrays cannot be mocked: " + mockFeatures.getTypeToMock() + ".",
                    ""
            ), generationFailed);
        }
        if (Modifier.isFinal(mockFeatures.getTypeToMock().getModifiers())) {
            throw new MockitoException(join(
                    "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                    "Can not mock final classes with the following settings :",
                    " - explicit serialization (e.g. withSettings().serializable())",
                    " - extra interfaces (e.g. withSettings().extraInterfaces(...))",
                    "",
                    "You are seeing this disclaimer because Mockito is configured to create inlined mocks.",
                    "You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.",
                    "",
                    "Underlying exception : " + generationFailed
            ), generationFailed);
        }
        if (Modifier.isPrivate(mockFeatures.getTypeToMock().getModifiers())) {
            throw new MockitoException(join(
                    "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                    "Most likely it is a private class that is not visible by Mockito",
                    "",
                    "You are seeing this disclaimer because Mockito is configured to create inlined mocks.",
                    "You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.",
                    ""
            ), generationFailed);
        }
        throw new MockitoException(join(
                "Mockito cannot mock this class: " + mockFeatures.getTypeToMock() + ".",
                "",
                "If you're not sure why you're getting this error, please report to the mailing list.",
                "",
                Platform.isJava8BelowUpdate45() ? "Java 8 early builds have bugs that were addressed in Java 1.8.0_45, please update your JDK!\n" : "",
                Platform.describe(),
                "",
                "You are seeing this disclaimer because Mockito is configured to create inlined mocks.",
                "You can learn about inline mocks and their limitations under item #39 of the Mockito class javadoc.",
                "",
                "Underlying exception : " + generationFailed
        ), generationFailed);
    }


    private static InternalMockHandler<?> asInternalMockHandler(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException(join(
                    "At the moment you cannot provide own implementations of MockHandler.",
                    "Please refer to the javadocs for the MockMaker interface.",
                    ""
            ));
        }
        return (InternalMockHandler<?>) handler;
    }

    @Override
    public MockHandler getHandler(Object mock) {
        MockMethodInterceptor interceptor = mocks.get(mock);
        if (interceptor == null) {
            return null;
        } else {
            return interceptor.handler;
        }
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        MockMethodInterceptor mockMethodInterceptor = new MockMethodInterceptor(asInternalMockHandler(newHandler), settings);
        mocks.put(mock, mockMethodInterceptor);
        if (mock instanceof MockAccess) {
            ((MockAccess) mock).setMockitoInterceptor(mockMethodInterceptor);
        }
    }

    @Override
    public TypeMockability isTypeMockable(final Class<?> type) {
        return new TypeMockability() {
            @Override
            public boolean mockable() {
                return instrumentation.isModifiableClass(type) && !EXCLUDES.contains(type);
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
                return "VM does not not support modification of given type";
            }
        };
    }
}
