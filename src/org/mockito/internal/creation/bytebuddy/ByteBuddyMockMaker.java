package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.ClassLoadingStrategy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.instrumentation.FieldAccessor;
import net.bytebuddy.instrumentation.MethodDelegation;
import net.bytebuddy.instrumentation.attribute.MethodAttributeAppender;
import net.bytebuddy.instrumentation.attribute.TypeAttributeAppender;
import net.bytebuddy.modifier.MemberVisibility;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.ObjectStreamException;
import java.util.*;

import static net.bytebuddy.instrumentation.method.matcher.MethodMatchers.*;

public class ByteBuddyMockMaker implements MockMaker {

    private static interface SilentConstructor {

        <T> T instantiate(Class<T> type);

        static class UsingObjenesis implements SilentConstructor {

            private final Objenesis objenesis;

            public UsingObjenesis(Objenesis objenesis) {
                this.objenesis = objenesis;
            }

            @Override
            public <T> T instantiate(Class<T> type) {
                return objenesis.newInstance(type);
            }
        }

        static enum Unavailable implements SilentConstructor {

            INSTANCE;

            @Override
            public <T> T instantiate(Class<T> type) {
                throw new MockitoException("Could not create mock: Objenesis is missing on the class path");
            }
        }
    }

    private static class MockKey {

        private final Class<?> mockType;
        private final Set<Class> types;
        private final boolean acrossClassLoaderSerialization;

        private MockKey(Class<?> mockType, Set<Class> interfaces, boolean acrossClassLoaderSerialization) {
            this.mockType = mockType;
            this.types = new HashSet<Class>(interfaces);
            types.add(mockType);
            this.acrossClassLoaderSerialization = acrossClassLoaderSerialization;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;

            MockKey mockKey = (MockKey) other;

            if (acrossClassLoaderSerialization != mockKey.acrossClassLoaderSerialization) return false;
            if (!mockType.equals(mockKey.mockType)) return false;
            if (!types.equals(mockKey.types)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = mockType.hashCode();
            result = 31 * result + types.hashCode();
            result = 31 * result + (acrossClassLoaderSerialization ? 1 : 0);
            return result;
        }
    }

    private final SilentConstructor silentConstructor;

    private final ByteBuddy byteBuddy;

    private final Map<MockKey, Class<?>> previousClasses;

    private final Random random;

    public ByteBuddyMockMaker() {
        silentConstructor = makeSilentConstructor();
        byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V6)
                .withDefaultMethodAttributeAppender(MethodAttributeAppender.ForInstrumentedMethod.INSTANCE)
                .withAttribute(TypeAttributeAppender.ForSuperType.INSTANCE);
        previousClasses = new HashMap<MockKey, Class<?>>();
        random = new Random();
    }

    private static SilentConstructor makeSilentConstructor() {
        try {
            return new SilentConstructor.UsingObjenesis(new ObjenesisStd(new GlobalConfiguration().enableClassCache()));
        } catch (Throwable throwable) {
            return SilentConstructor.Unavailable.INSTANCE;
        }
    }

    @Override
    public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
        Class<? extends T> mockedType = getOrMakeMock(settings.getTypeToMock(),
                settings.getExtraInterfaces(),
                settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS);
        T mock = silentConstructor.instantiate(mockedType);
        MethodInterceptor.Access access = (MethodInterceptor.Access) mock;
        access.setMockitoInterceptor(new MethodInterceptor(asInternalMockHandler(handler), settings));
        return mock;
    }

    private <T> Class<? extends T> getOrMakeMock(Class<T> mockType,
                                                 Set<Class> interfaces,
                                                 boolean acrossClassLoaderSerialization) {
        MockKey mockKey = new MockKey(mockType, interfaces, acrossClassLoaderSerialization);
        @SuppressWarnings("unchecked")
        Class<? extends T> mockedType = (Class<? extends T>) previousClasses.get(mockKey);
        if (mockedType == null) {
            mockedType = makeMock(mockType, interfaces, acrossClassLoaderSerialization);
            previousClasses.put(mockKey, mockedType);
        }
        return mockedType;
    }

    private <T> Class<? extends T> makeMock(Class<T> mockType,
                                            Set<Class> interfaces,
                                            boolean acrossClassLoaderSerialization) {
        DynamicType.Builder<T> builder = byteBuddy.subclass(mockType)
                .name(nameFor(mockType))
                .implement(interfaces.toArray(new Class<?>[interfaces.size()]))
                .method(any()).intercept(MethodDelegation.toInstanceField(MethodInterceptor.class, "mockitoInterceptor"))
                .implement(MethodInterceptor.Access.class).intercept(FieldAccessor.ofBeanProperty())
                .method(isHashCode()).intercept(MethodDelegation.to(MethodInterceptor.ForHashCode.class))
                .method(isEquals()).intercept(MethodDelegation.to(MethodInterceptor.ForEquals.class));
        if (acrossClassLoaderSerialization) {
            builder = builder.defineMethod("writeReplace", Object.class, Collections.<Class<?>>emptyList(), MemberVisibility.PRIVATE)
                    .throwing(ObjectStreamException.class)
                    .intercept(MethodDelegation.to(MethodDelegation.to(MethodInterceptor.ForWriteReplace.class)));
        }
        return builder.make()
                .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
    }

    private String nameFor(Class<?> type) {
        String typeName = type.getName();
        if (typeName.startsWith("java.") || type.getPackage().isSealed()) {
            typeName = "codegen." + typeName;
        }
        return String.format("%s$%s$%d", typeName, "MockitoMock", Math.abs(random.nextInt()));
    }

    @Override
    public MockHandler getHandler(Object mock) {
        if (!(mock instanceof MethodInterceptor.Access)) {
            return null;
        }
        return ((MethodInterceptor.Access) mock).getMockitoInterceptor().getMockHandler();
    }

    @Override
    public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
        ((MethodInterceptor.Access) mock).setMockitoInterceptor(new MethodInterceptor(asInternalMockHandler(newHandler), settings));
    }

    private static InternalMockHandler asInternalMockHandler(MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException("At the moment you cannot provide own implementations of MockHandler." +
                    "\nPlease see the javadocs for the MockMaker interface.");
        }
        return (InternalMockHandler) handler;
    }
}
