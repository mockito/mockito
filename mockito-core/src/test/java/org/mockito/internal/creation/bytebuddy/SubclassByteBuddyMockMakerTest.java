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
import org.junit.Test;
import org.mockito.MockMakers;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.plugins.MockMaker;
import org.mockito.stubbing.Answer;
import org.mockitoutil.ClassLoaders;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Semaphore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockitoutil.ClassLoaders.coverageTool;

public class SubclassByteBuddyMockMakerTest
        extends AbstractByteBuddyMockMakerTest<SubclassByteBuddyMockMaker> {

    public SubclassByteBuddyMockMakerTest() {
        super(new SubclassByteBuddyMockMaker());
    }

    @Test
    public void can_create_mock_with_default_allocator_in_an_isolated_class_loader()
            throws Exception {
        ClassLoader classLoader = mockitoClassLoader().build();

        Semaphore mock = ClassLoaders.using(classLoader).execute(() -> mockSemaphore(classLoader));

        assertThat(mock.getClass().getSuperclass()).isEqualTo(Semaphore.class);
    }

    @Test
    public void can_create_mock_with_constructor_when_unsafe_is_unavailable() throws Exception {
        ClassLoader classLoader = mockitoClassLoader().without("sun.misc.Unsafe").build();

        assertThatExceptionOfType(ClassNotFoundException.class)
                .isThrownBy(() -> classLoader.loadClass("sun.misc.Unsafe"));
        int permits =
                ClassLoaders.using(classLoader)
                        .execute(() -> mockSemaphore(classLoader, 3).availablePermits());

        assertThat(permits).isEqualTo(3);
    }

    private static ClassLoaders.ExcludingURLClassLoaderBuilder mockitoClassLoader() {
        return ClassLoaders.excludingClassLoader()
                .withCodeSourceUrlOf(Mockito.class, ByteBuddy.class, ByteBuddyAgent.class)
                .withCodeSourceUrlOf(coverageTool());
    }

    private static Semaphore mockSemaphore(ClassLoader classLoader, Object... constructorArgs)
            throws ReflectiveOperationException {
        // Resolve Mockito and its settings API in the isolated loader used by the test.
        Class<?> mockito = classLoader.loadClass(Mockito.class.getName());
        Class<?> settingsType = classLoader.loadClass(MockSettings.class.getName());
        Object settings = mockito.getMethod("withSettings").invoke(null);
        settingsType.getMethod("mockMaker", String.class).invoke(settings, MockMakers.SUBCLASS);
        if (constructorArgs.length > 0) {
            settingsType
                    .getMethod("useConstructor", Object[].class)
                    .invoke(settings, (Object) constructorArgs);
            Class<?> answerType = classLoader.loadClass(Answer.class.getName());
            settingsType
                    .getMethod("defaultAnswer", answerType)
                    .invoke(settings, mockito.getField("CALLS_REAL_METHODS").get(null));
        }
        return (Semaphore)
                mockito.getMethod("mock", Class.class, settingsType)
                        .invoke(null, Semaphore.class, settings);
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
