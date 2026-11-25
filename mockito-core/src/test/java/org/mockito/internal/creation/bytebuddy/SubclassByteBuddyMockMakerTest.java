/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.dynamic.DynamicType;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.MockMakers;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.plugins.MockMaker;
import org.mockitoutil.ClassLoaders;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Observable;
import java.util.Observer;
import java.util.function.IntSupplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class SubclassByteBuddyMockMakerTest
        extends AbstractByteBuddyMockMakerTest<SubclassByteBuddyMockMaker> {

    public SubclassByteBuddyMockMakerTest() {
        super(new SubclassByteBuddyMockMaker());
    }

    @Test
    public void can_create_mock_with_default_allocator_in_an_isolated_class_loader()
            throws Exception {
        ClassLoader classLoader = mockitoClassLoader().build();

        IntSupplier mock = ClassLoaders.using(classLoader).execute(() -> mockAbstractClass());

        assertThat(mock.getAsInt()).isZero();
    }

    @Test
    public void can_create_mock_with_constructor_when_unsafe_is_unavailable() throws Exception {
        ClassLoader classLoader = mockitoClassLoader().without("sun.misc.Unsafe").build();

        assertThatExceptionOfType(ClassNotFoundException.class)
                .isThrownBy(() -> classLoader.loadClass("sun.misc.Unsafe"));
        IntSupplier mock = ClassLoaders.using(classLoader).execute(() -> mockAbstractClass(3));

        assertThat(mock.getAsInt()).isEqualTo(3);
    }

    private static ClassLoaders.ExcludingURLClassLoaderBuilder mockitoClassLoader() {
        return ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(
                        Mockito.class,
                        ByteBuddy.class,
                        ByteBuddyAgent.class,
                        AbstractClassToMock.class)
                // Naming the nested mock type links its enclosing test class.
                .withCodeSourceUrlOf(ClassLoaders.class, Assertions.class);
    }

    private static IntSupplier mockAbstractClass(Object... constructorArgs) {
        MockSettings settings = withSettings().mockMaker(MockMakers.SUBCLASS);
        if (constructorArgs.length > 0) {
            settings.useConstructor(constructorArgs);
        }
        return mock(AbstractClassToMock.class, settings);
    }

    public abstract static class AbstractClassToMock implements IntSupplier {
        private final int value;

        public AbstractClassToMock(int value) {
            this.value = value;
        }

        @Override
        public final int getAsInt() {
            return value;
        }
    }

    @Test
    public void is_type_mockable_excludes_primitive_wrapper_classes() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(Integer.class);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("final");
    }

    @Test
    public void is_type_mockable_excludes_sealed_classes() {
        if (ClassFileVersion.ofThisVm().isAtMost(ClassFileVersion.JAVA_V16)) {
            return;
        }
        DynamicType.Builder<?> base = new ByteBuddy().subclass(Object.class);
        DynamicType.Builder<?> subclass =
                new ByteBuddy().subclass(base.toTypeDescription()).merge(TypeManifestation.FINAL);
        Class<?> type =
                base.permittedSubclass(subclass.toTypeDescription())
                        .make()
                        .include(subclass.make())
                        .load(null)
                        .getLoaded();
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(type);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("sealed");
    }

    @Test
    public void is_type_mockable_excludes_primitive_classes() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(int.class);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("primitive");
    }

    @Test
    public void is_type_mockable_allow_anonymous() {
        Observer anonymous =
                new Observer() {
                    @Override
                    public void update(Observable o, Object arg) {}
                };
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(anonymous.getClass());
        assertThat(mockable.mockable()).isTrue();
        assertThat(mockable.nonMockableReason()).contains("");
    }

    @Test
    public void is_type_mockable_give_empty_reason_if_type_is_mockable() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(SomeClass.class);
        assertThat(mockable.mockable()).isTrue();
        assertThat(mockable.nonMockableReason()).isEqualTo("");
    }

    @Test
    public void mock_class_with_annotations() throws Exception {
        MockSettingsImpl<ClassWithAnnotation> mockSettings = new MockSettingsImpl<>();
        mockSettings.setTypeToMock(ClassWithAnnotation.class);

        ClassWithAnnotation proxy = mockMaker.createMock(mockSettings, dummyHandler());

        assertThat(proxy.getClass().isAnnotationPresent(SampleAnnotation.class)).isTrue();
        assertThat(proxy.getClass().getAnnotation(SampleAnnotation.class).value()).isEqualTo("foo");

        assertThat(
                        proxy.getClass()
                                .getMethod("sampleMethod")
                                .isAnnotationPresent(SampleAnnotation.class))
                .isTrue();
        assertThat(
                        proxy.getClass()
                                .getMethod("sampleMethod")
                                .getAnnotation(SampleAnnotation.class)
                                .value())
                .isEqualTo("bar");
    }

    @Test
    public void mock_class_with_annotations_with_additional_interface() throws Exception {
        MockSettingsImpl<ClassWithAnnotation> mockSettings = new MockSettingsImpl<>();
        mockSettings.setTypeToMock(ClassWithAnnotation.class);
        mockSettings.extraInterfaces(Serializable.class);

        ClassWithAnnotation proxy = mockMaker.createMock(mockSettings, dummyHandler());

        assertThat(proxy.getClass().isAnnotationPresent(SampleAnnotation.class)).isTrue();
        assertThat(proxy.getClass().getAnnotation(SampleAnnotation.class).value()).isEqualTo("foo");

        assertThat(
                        proxy.getClass()
                                .getMethod("sampleMethod")
                                .isAnnotationPresent(SampleAnnotation.class))
                .isTrue();
        assertThat(
                        proxy.getClass()
                                .getMethod("sampleMethod")
                                .getAnnotation(SampleAnnotation.class)
                                .value())
                .isEqualTo("bar");
    }

    @Test
    public void mock_interface_with_annotations() throws Exception {
        MockSettingsImpl<InterfaceWithAnnotation> mockSettings = new MockSettingsImpl<>();
        mockSettings.setTypeToMock(InterfaceWithAnnotation.class);

        InterfaceWithAnnotation proxy = mockMaker.createMock(mockSettings, dummyHandler());

        assertThat(proxy.getClass().isAnnotationPresent(SampleAnnotation.class)).isTrue();
        assertThat(proxy.getClass().getAnnotation(SampleAnnotation.class).value()).isEqualTo("foo");

        assertThat(
                        proxy.getClass()
                                .getMethod("sampleMethod")
                                .isAnnotationPresent(SampleAnnotation.class))
                .isTrue();
        assertThat(
                        proxy.getClass()
                                .getMethod("sampleMethod")
                                .getAnnotation(SampleAnnotation.class)
                                .value())
                .isEqualTo("bar");
    }

    @Test
    public void mock_interface_with_annotations_with_additional_interface() throws Exception {
        MockSettingsImpl<InterfaceWithAnnotation> mockSettings = new MockSettingsImpl<>();
        mockSettings.setTypeToMock(InterfaceWithAnnotation.class);
        mockSettings.extraInterfaces(Serializable.class);

        InterfaceWithAnnotation proxy = mockMaker.createMock(mockSettings, dummyHandler());

        assertThat(proxy.getClass().isAnnotationPresent(SampleAnnotation.class)).isFalse();

        assertThat(
                        proxy.getClass()
                                .getMethod("sampleMethod")
                                .isAnnotationPresent(SampleAnnotation.class))
                .isTrue();
        assertThat(
                        proxy.getClass()
                                .getMethod("sampleMethod")
                                .getAnnotation(SampleAnnotation.class)
                                .value())
                .isEqualTo("bar");
    }

    @Test
    public void mock_type_without_annotations() throws Exception {
        MockSettingsImpl<ClassWithAnnotation> mockSettings = new MockSettingsImpl<>();
        mockSettings.setTypeToMock(ClassWithAnnotation.class);
        mockSettings.withoutAnnotations();

        ClassWithAnnotation proxy = mockMaker.createMock(mockSettings, dummyHandler());

        assertThat(proxy.getClass().isAnnotationPresent(SampleAnnotation.class)).isFalse();
        assertThat(
                        proxy.getClass()
                                .getMethod("sampleMethod")
                                .isAnnotationPresent(SampleAnnotation.class))
                .isFalse();
    }

    @Override
    protected Class<?> mockTypeOf(Class<?> type) {
        return type.getSuperclass();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface SampleAnnotation {

        String value();
    }

    @SampleAnnotation("foo")
    public static class ClassWithAnnotation {

        @SampleAnnotation("bar")
        public void sampleMethod() {
            throw new UnsupportedOperationException();
        }
    }

    @SampleAnnotation("foo")
    public interface InterfaceWithAnnotation {

        @SampleAnnotation("bar")
        void sampleMethod();
    }
}
