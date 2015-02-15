package org.mockito.internal.creation.bytebuddy;

import static net.bytebuddy.instrumentation.method.matcher.MethodMatchers.any;
import static net.bytebuddy.instrumentation.method.matcher.MethodMatchers.isDeclaredBy;
import static net.bytebuddy.instrumentation.method.matcher.MethodMatchers.isEquals;
import static net.bytebuddy.instrumentation.method.matcher.MethodMatchers.isHashCode;
import java.util.Random;
import java.util.Set;
import org.mockito.internal.creation.util.SearchingClassLoader;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.ClassLoadingStrategy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.instrumentation.FieldAccessor;
import net.bytebuddy.instrumentation.MethodDelegation;
import net.bytebuddy.instrumentation.attribute.MethodAttributeAppender;
import net.bytebuddy.instrumentation.attribute.TypeAttributeAppender;
import net.bytebuddy.modifier.FieldManifestation;
import net.bytebuddy.modifier.Ownership;
import net.bytebuddy.modifier.Visibility;

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

    public <T> Class<? extends T> generateMockClass(Class<T> mockedType, Set<Class> interfaces) {
        DynamicType.Builder<T> builder = byteBuddy.subclass(mockedType, ConstructorStrategy.Default.IMITATE_SUPER_TYPE)
                .name(nameFor(mockedType))
                .implement(interfaces.toArray(new Class<?>[interfaces.size()]))
                .method(any()).intercept(MethodDelegation
                        .toInstanceField(MockMethodInterceptor.class, "mockitoInterceptor")
                        .filter(isDeclaredBy(MockMethodInterceptor.class)))
                .implement(MockMethodInterceptor.MockAccess.class).intercept(FieldAccessor.ofBeanProperty())
                .method(isHashCode()).intercept(MethodDelegation.to(MockMethodInterceptor.ForHashCode.class))
                .method(isEquals()).intercept(MethodDelegation.to(MockMethodInterceptor.ForEquals.class))
                .defineField("serialVersionUID", long.class, Ownership.STATIC, Visibility.PRIVATE, FieldManifestation.FINAL).value(42L);
//            if (acrossClassLoaderSerialization) {
//                builder = builder.implement(AcrossJVMSerializationFeature.AcrossJVMMockitoMockSerializable.class)
//                        .intercept(MethodDelegation.to(MethodInterceptor.ForWriteReplace.class));
//            }
        Class<?>[] allMockedTypes = new Class<?>[interfaces.size() + 1];
        allMockedTypes[0] = mockedType;
//            System.arraycopy(interfaces.toArray(), 0, allMockedTypes, 1, interfaces.size());
        int index = 1;
        for (Class<?> type : interfaces) {
            allMockedTypes[index++] = type;
        }
        return builder.make()
                .load(SearchingClassLoader.combineLoadersOf(allMockedTypes), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
    }

    // TODO inspect naming strategy (for OSGI, signed package, java.* (and bootstrap classes), etc...)
    private String nameFor(Class<?> type) {
        String typeName = type.getName();
        if (typeName.startsWith("java.") || (type.getPackage() != null && type.getPackage().isSealed())) {
            typeName = "codegen." + typeName;
        }
        return String.format("%s$%s$%d", typeName, "MockitoMock", Math.abs(random.nextInt()));
    }
}
