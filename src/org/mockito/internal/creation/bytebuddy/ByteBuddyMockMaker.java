package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.ClassLoadingStrategy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.instrumentation.FieldAccessor;
import net.bytebuddy.instrumentation.MethodDelegation;
import net.bytebuddy.instrumentation.attribute.MethodAttributeAppender;
import net.bytebuddy.instrumentation.attribute.TypeAttributeAppender;
import net.bytebuddy.modifier.MemberVisibility;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.jmock.SearchingClassLoader;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.ObjectStreamException;
import java.lang.reflect.Modifier;
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

        private final Class<?> mockedType;
        private final Set<Class> types;
        private final boolean acrossClassLoaderSerialization;

        private MockKey(Class<?> mockedType, Set<Class> interfaces, boolean acrossClassLoaderSerialization) {
            this.mockedType = mockedType;
            this.types = new HashSet<Class>(interfaces);
            types.add(mockedType);
            this.acrossClassLoaderSerialization = acrossClassLoaderSerialization;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;

            MockKey mockKey = (MockKey) other;

            if (acrossClassLoaderSerialization != mockKey.acrossClassLoaderSerialization) return false;
            if (!mockedType.equals(mockKey.mockedType)) return false;
            if (!types.equals(mockKey.types)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = mockedType.hashCode();
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

    private <T> Class<? extends T> getOrMakeMock(Class<T> mockedType,
                                                 Set<Class> interfaces,
                                                 boolean acrossClassLoaderSerialization) {
        MockKey mockKey = new MockKey(mockedType, interfaces, acrossClassLoaderSerialization);
        @SuppressWarnings("unchecked")
        Class<? extends T> mockType = (Class<? extends T>) previousClasses.get(mockKey);
        if (mockType == null) {
            try {
                mockType = makeMock(mockedType, interfaces, acrossClassLoaderSerialization);
            } catch (Exception e) {
                prettify(mockedType, e);
            }
            previousClasses.put(mockKey, mockType);
        }
        return mockType;
    }

    private static void prettify(Class<?> mockedType, Exception e) {
        if (Modifier.isPrivate(mockedType.getModifiers())) {
            throw new MockitoException("\n"
                    + "Mockito cannot mock this class: " + mockedType
                    + ".\n"
                    + "Most likely it is a private class that is not visible by Mockito");
        }
        throw new MockitoException("\n"
                + "Mockito cannot mock this class: " + mockedType
                + "\n"
                + "Mockito can only mock visible & non-final classes."
                + "\n"
                + "If you're not sure why you're getting this error, please report to the mailing list.", e);
    }

    private <T> Class<? extends T> makeMock(Class<T> mockedType,
                                            Set<Class> interfaces,
                                            boolean acrossClassLoaderSerialization) {
        DynamicType.Builder<T> builder = byteBuddy.subclass(mockedType, ConstructorStrategy.Default.NO_CONSTRUCTORS)
                .name(nameFor(mockedType))
                .implement(interfaces.toArray(new Class<?>[interfaces.size()]))
                .method(any()).intercept(MethodDelegation
                        .toInstanceField(MethodInterceptor.class, "mockitoInterceptor")
                        .filter(isDeclaredBy(MethodInterceptor.class)))
                .implement(MethodInterceptor.Access.class).intercept(FieldAccessor.ofBeanProperty())
                .method(isHashCode()).intercept(MethodDelegation.to(MethodInterceptor.ForHashCode.class))
                .method(isEquals()).intercept(MethodDelegation.to(MethodInterceptor.ForEquals.class));
        if (acrossClassLoaderSerialization) {
            builder = builder.defineMethod("writeReplace", Object.class, Collections.<Class<?>>emptyList(), MemberVisibility.PRIVATE)
                    .throwing(ObjectStreamException.class)
                    .intercept(MethodDelegation.to(MethodInterceptor.ForWriteReplace.class));
        }
        Class<?>[] allMockedTypes = new Class<?>[interfaces.size() + 1];
        allMockedTypes[0] = mockedType;
        int index = 1;
        for (Class<?> type : interfaces) {
            allMockedTypes[index++] = type;
        }
        return builder.make()
                .load(SearchingClassLoader.combineLoadersOf(allMockedTypes), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
    }

    private String nameFor(Class<?> type) {
        String typeName = type.getName();
        if (typeName.startsWith("java.") || (type.getPackage() != null && type.getPackage().isSealed())) {
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
