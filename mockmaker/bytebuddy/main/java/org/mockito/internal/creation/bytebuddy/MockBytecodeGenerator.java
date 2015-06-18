package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.implementation.attribute.TypeAttributeAppender;
import org.mockito.internal.creation.bytebuddy.ByteBuddyCrossClassLoaderSerializationSupport.CrossClassLoaderSerializableMock;
import org.mockito.internal.creation.util.SearchingClassLoader;

import java.util.Random;

import static net.bytebuddy.description.modifier.FieldManifestation.FINAL;
import static net.bytebuddy.description.modifier.Ownership.STATIC;
import static net.bytebuddy.description.modifier.Visibility.PRIVATE;
import static net.bytebuddy.implementation.FieldAccessor.ofBeanProperty;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.implementation.MethodDelegation.toInstanceField;
import static net.bytebuddy.matcher.ElementMatchers.*;

class MockBytecodeGenerator {
    private final ByteBuddy byteBuddy;
    private final Random random;

    public MockBytecodeGenerator() {
        byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V5)
//                .withIgnoredMethods(isBridge())
                .withDefaultMethodAttributeAppender(MethodAttributeAppender.ForInstrumentedMethod.INSTANCE)
                .withAttribute(TypeAttributeAppender.ForSuperType.INSTANCE);

        random = new Random();
    }

    public <T> Class<? extends T> generateMockClass(MockFeatures<T> features) {
        DynamicType.Builder<T> builder =
                byteBuddy.subclass(features.mockedType, ConstructorStrategy.Default.IMITATE_SUPER_TYPE)
                         .name(nameFor(features.mockedType))
                         .implement(features.interfaces.toArray(new Class<?>[features.interfaces.size()]))
                         .method(any()).intercept(toInstanceField(MockMethodInterceptor.class, "mockitoInterceptor")
                                                          .filter(isDeclaredBy(MockMethodInterceptor.class)))
                         .implement(MockMethodInterceptor.MockAccess.class).intercept(ofBeanProperty())
                         .method(isHashCode()).intercept(to(MockMethodInterceptor.ForHashCode.class))
                         .method(isEquals()).intercept(to(MockMethodInterceptor.ForEquals.class))
                         .defineField("serialVersionUID", long.class, STATIC, PRIVATE, FINAL).value(42L);
        if (features.crossClassLoaderSerializable) {
            builder = builder.implement(CrossClassLoaderSerializableMock.class)
                             .intercept(to(MockMethodInterceptor.ForWriteReplace.class));
        }
        Class<?>[] allMockedTypes = new Class<?>[features.interfaces.size() + 1];
        allMockedTypes[0] = features.mockedType;
//            System.arraycopy(interfaces.toArray(), 0, allMockedTypes, 1, interfaces.size());
        int index = 1;
        for (Class<?> type : features.interfaces) {
            allMockedTypes[index++] = type;
        }
        return builder.make()
                      .load(SearchingClassLoader.combineLoadersOf(allMockedTypes), ClassLoadingStrategy.Default.INJECTION)
                      .getLoaded();
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
