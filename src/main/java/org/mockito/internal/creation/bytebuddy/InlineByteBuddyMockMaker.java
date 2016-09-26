package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.ClassFileLocator;
import org.mockito.Incubating;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.instance.Instantiator;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static org.mockito.internal.creation.bytebuddy.InlineBytecodeGenerator.EXCLUDES;
import static org.mockito.internal.util.StringJoiner.join;

@Incubating
public class InlineByteBuddyMockMaker implements MockMaker {

    private final Instrumentation instrumentation;

    private final BytecodeGenerator bytecodeGenerator;

    private final WeakConcurrentMap<Object, MockMethodInterceptor> mocks = new WeakConcurrentMap.WithInlinedExpunction<Object, MockMethodInterceptor>();

    public InlineByteBuddyMockMaker() {
        try {
            instrumentation = ByteBuddyAgent.install();
            if (!instrumentation.isRetransformClassesSupported()) {
                throw new MockitoException("Current VM does not support retransformation");
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
            bytecodeGenerator = new TypeCachingBytecodeGenerator(new InlineBytecodeGenerator(instrumentation, mocks), true);
        } catch (IOException exception) {
            throw new MockitoException("Cannot apply self-instrumentation on current VM", exception);
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
        return bytecodeGenerator.mockClass(MockFeatures.withMockFeatures(
                settings.getTypeToMock(),
                settings.getExtraInterfaces(),
                settings.getSerializableMode()
        ));
    }

    private static InternalMockHandler<?> asInternalMockHandler(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException(join(
                    "At the moment you cannot provide own implementations of MockHandler.",
                    "Please see the javadocs for the MockMaker interface.",
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
