package org.mockito.internal.creation.bytebuddy;

import static org.mockito.internal.util.StringJoiner.join;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.instance.Instantiator;
import org.mockito.internal.creation.instance.InstantiatorProvider;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

@SuppressWarnings("rawtypes")
public class ByteBuddyMockMaker implements MockMaker {

    private final CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    public ByteBuddyMockMaker() {
        cachingMockBytecodeGenerator = new CachingMockBytecodeGenerator();
    }

    public <T> T createMock(final MockCreationSettings<T> settings, final MockHandler handler) {
        final Class<T> mockedProxyType = createProxyClass(mockWithFeaturesFrom(settings));

        final Instantiator instantiator = new InstantiatorProvider().getInstantiator(settings);
        T mockInstance = null;
        try {
            mockInstance = instantiator.newInstance(mockedProxyType);
            final MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
            mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));

            return ensureMockIsAssignableToMockedType(settings, mockInstance);
        } catch (final ClassCastException cce) {
            throw new MockitoException(join(
                    "ClassCastException occurred while creating the mockito mock :",
                    "  class to mock : " + describeClass(mockedProxyType),
                    "  created class : " + describeClass(settings.getTypeToMock()),
                    "  proxy instance class : " + describeClass(mockInstance),
                    "  instance creation by : " + instantiator.getClass().getSimpleName(),
                    "",
                    "You might experience classloading issues, please ask the mockito mailing-list.",
                    ""
            ),cce);
        } catch (final org.mockito.internal.creation.instance.InstantiationException e) {
            throw new MockitoException("Unable to create mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
        }
    }

    <T> Class<T> createProxyClass(final MockFeatures<T> mockFeatures) {
        return cachingMockBytecodeGenerator.get(mockFeatures);
    }


    private <T> MockFeatures<T> mockWithFeaturesFrom(final MockCreationSettings<T> settings) {
        return MockFeatures.withMockFeatures(
                settings.getTypeToMock(),
                settings.getExtraInterfaces(),
                settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS
        );
    }

    private <T> T ensureMockIsAssignableToMockedType(final MockCreationSettings<T> settings, final T mock) {
        // Force explicit cast to mocked type here, instead of
        // relying on the JVM to implicitly cast on the client call site.
        // This allows us to catch the ClassCastException earlier
        final Class<T> typeToMock = settings.getTypeToMock();
        return typeToMock.cast(mock);
    }

    private static String describeClass(final Class type) {
        return type == null ? "null" : "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }

    private static String describeClass(final Object instance) {
        return instance == null ? "null" : describeClass(instance.getClass());
    }

    public MockHandler getHandler(final Object mock) {
        if (!(mock instanceof MockMethodInterceptor.MockAccess)) {
            return null;
        }
        return ((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getMockHandler();
    }

    public void resetMock(final Object mock, final MockHandler newHandler, final MockCreationSettings settings) {
        ((MockMethodInterceptor.MockAccess) mock).setMockitoInterceptor(
                new MockMethodInterceptor(asInternalMockHandler(newHandler), settings)
        );
    }

    private static InternalMockHandler asInternalMockHandler(final MockHandler handler) {
        if (!(handler instanceof InternalMockHandler)) {
            throw new MockitoException(join(
                    "At the moment you cannot provide own implementations of MockHandler.",
                    "Please see the javadocs for the MockMaker interface.",
                    ""
            ));
        }
        return (InternalMockHandler) handler;
    }
}
