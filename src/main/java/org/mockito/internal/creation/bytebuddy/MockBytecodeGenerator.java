package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.SynchronizationState;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.MultipleParentClassLoader;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport.CrossClassLoaderSerializableMock;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor.DispatcherDefaultingToRealMethod;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor.MockAccess;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import static net.bytebuddy.description.modifier.Visibility.PRIVATE;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.*;

class MockBytecodeGenerator {

    private final ByteBuddy byteBuddy;
    private final Random random;

    public MockBytecodeGenerator() {
        byteBuddy = new ByteBuddy().with(TypeValidation.DISABLED);
        random = new Random();
    }

    public <T> Class<? extends T> generateMockClass(MockFeatures<T> features) {
        DynamicType.Builder<T> builder =
                byteBuddy.subclass(features.mockedType)
                         .name(nameFor(features.mockedType))
                         .ignoreAlso(isGroovyMethod())
                         .annotateType(features.mockedType.getAnnotations())
                         .implement(new ArrayList<Type>(features.interfaces))
                         .method(any())
                           .intercept(MethodDelegation.to(DispatcherDefaultingToRealMethod.class))
                           .transform(Transformer.ForMethod.withModifiers(SynchronizationState.PLAIN))
                           .attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
                         .serialVersionUid(42L)
                         .defineField("mockitoInterceptor", MockMethodInterceptor.class, PRIVATE)
                         .implement(MockAccess.class)
                           .intercept(FieldAccessor.ofBeanProperty())
                         .method(isHashCode())
                           .intercept(to(MockMethodInterceptor.ForHashCode.class))
                         .method(isEquals())
                           .intercept(to(MockMethodInterceptor.ForEquals.class));
        if (features.crossClassLoaderSerializable) {
            builder = builder.implement(CrossClassLoaderSerializableMock.class)
                             .intercept(to(MockMethodInterceptor.ForWriteReplace.class));
        }
        return builder.make()
                      .load(new MultipleParentClassLoader.Builder()
                              .append(features.mockedType)
                              .append(features.interfaces)
                              .append(Thread.currentThread().getContextClassLoader())
                              .append(MockAccess.class, DispatcherDefaultingToRealMethod.class)
                              .append(MockMethodInterceptor.class,
                                      MockMethodInterceptor.ForHashCode.class,
                                      MockMethodInterceptor.ForEquals.class).build(),
                              ClassLoadingStrategy.Default.INJECTION.with(features.mockedType.getProtectionDomain()))
                      .getLoaded();
    }

    private static ElementMatcher<MethodDescription> isGroovyMethod() {
        return isDeclaredBy(named("groovy.lang.GroovyObjectSupport"));
    }

    // TODO inspect naming strategy (for OSGI, signed package, java.* (and bootstrap classes), etc...)
    private String nameFor(Class<?> type) {
        String typeName = type.getName();
        if (isComingFromJDK(type)
                || isComingFromSignedJar(type)
                || isComingFromSealedPackage(type)) {
            typeName = "codegen." + typeName;
        }
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

    private boolean isComingFromSignedJar(Class<?> type) {
        return type.getSigners() != null;
    }
}
