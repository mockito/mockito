/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.StubMethod;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.util.collections.Sets;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import java.util.*;
import java.util.regex.Pattern;

import static net.bytebuddy.ClassFileVersion.JAVA_V8;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assume.assumeTrue;

public class InlineByteBuddyMockMakerTest extends AbstractByteBuddyMockMakerTest<InlineByteBuddyMockMaker> {

    public InlineByteBuddyMockMakerTest() {
        super(new InlineByteBuddyMockMaker());
    }

    @Override
    protected Class<?> mockTypeOf(Class<?> type) {
        return type;
    }

    @Test
    public void should_create_mock_from_final_class() throws Exception {
        MockCreationSettings<FinalClass> settings = settingsFor(FinalClass.class);
        FinalClass proxy = mockMaker.createMock(settings, new MockHandlerImpl<FinalClass>(settings));
        assertThat(proxy.foo()).isEqualTo("bar");
    }

    @Test
    public void should_create_mock_from_final_class_in_the_JDK() throws Exception {
        MockCreationSettings<Pattern> settings = settingsFor(Pattern.class);
        Pattern proxy = mockMaker.createMock(settings, new MockHandlerImpl<Pattern>(settings));
        assertThat(proxy.pattern()).isEqualTo("bar");
    }

    @Test
    public void should_create_mock_from_abstract_class_with_final_method() throws Exception {
        MockCreationSettings<FinalMethodAbstractType> settings = settingsFor(FinalMethodAbstractType.class);
        FinalMethodAbstractType proxy = mockMaker.createMock(settings, new MockHandlerImpl<FinalMethodAbstractType>(settings));
        assertThat(proxy.foo()).isEqualTo("bar");
        assertThat(proxy.bar()).isEqualTo("bar");
    }

    @Test
    public void should_create_mock_from_final_class_with_interface_methods() throws Exception {
        MockCreationSettings<FinalMethod> settings = settingsFor(FinalMethod.class, SampleInterface.class);
        FinalMethod proxy = mockMaker.createMock(settings, new MockHandlerImpl<FinalMethod>(settings));
        assertThat(proxy.foo()).isEqualTo("bar");
        assertThat(((SampleInterface) proxy).bar()).isEqualTo("bar");
    }

    @Test
    public void should_detect_non_overridden_generic_method_of_supertype() throws Exception {
        MockCreationSettings<GenericSubClass> settings = settingsFor(GenericSubClass.class);
        GenericSubClass proxy = mockMaker.createMock(settings, new MockHandlerImpl<GenericSubClass>(settings));
        assertThat(proxy.value()).isEqualTo("bar");
    }

    @Test
    public void should_create_mock_from_hashmap() throws Exception {
        MockCreationSettings<HashMap> settings = settingsFor(HashMap.class);
        HashMap proxy = mockMaker.createMock(settings, new MockHandlerImpl<HashMap>(settings));
        assertThat(proxy.get(null)).isEqualTo("bar");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_throw_exception_redefining_unmodifiable_class() {
        MockCreationSettings settings = settingsFor(int.class);
        try {
            mockMaker.createMock(settings, new MockHandlerImpl(settings));
            fail("Expected a MockitoException");
        } catch (MockitoException e) {
            e.printStackTrace();
            assertThat(e).hasMessageContaining("Could not modify all classes");
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_throw_exception_redefining_array() {
        int[] array = new int[5];
        MockCreationSettings<? extends int[]> settings = settingsFor(array.getClass());
        try {
            mockMaker.createMock(settings, new MockHandlerImpl(settings));
            fail("Expected a MockitoException");
        } catch (MockitoException e) {
            assertThat(e).hasMessageContaining("Arrays cannot be mocked");
        }
    }

    @Test
    public void should_create_mock_from_enum() throws Exception {
        MockCreationSettings<EnumClass> settings = settingsFor(EnumClass.class);
        EnumClass proxy = mockMaker.createMock(settings, new MockHandlerImpl<EnumClass>(settings));
        assertThat(proxy.foo()).isEqualTo("bar");
    }

    @Test
    public void should_fail_at_creating_a_mock_of_a_final_class_with_explicit_serialization() throws Exception {
        MockCreationSettings<FinalClass> settings = new CreationSettings<FinalClass>()
                .setTypeToMock(FinalClass.class)
                .setSerializableMode(SerializableMode.BASIC);

        try {
            mockMaker.createMock(settings, new MockHandlerImpl<FinalClass>(settings));
            fail("Expected a MockitoException");
        } catch (MockitoException e) {
            assertThat(e)
                    .hasMessageContaining("Unsupported settings")
                    .hasMessageContaining("serialization")
                    .hasMessageContaining("extra interfaces");
        }
    }

    @Test
    public void should_fail_at_creating_a_mock_of_a_final_class_with_extra_interfaces() throws Exception {
        MockCreationSettings<FinalClass> settings = new CreationSettings<FinalClass>()
                .setTypeToMock(FinalClass.class)
                .setExtraInterfaces(Sets.<Class<?>>newSet(List.class));

        try {
            mockMaker.createMock(settings, new MockHandlerImpl<FinalClass>(settings));
            fail("Expected a MockitoException");
        } catch (MockitoException e) {
            assertThat(e)
                    .hasMessageContaining("Unsupported settings")
                    .hasMessageContaining("serialization")
                    .hasMessageContaining("extra interfaces");
        }
    }

    @Test
    public void should_mock_interface() {
        MockSettingsImpl<Set> mockSettings = new MockSettingsImpl<Set>();
        mockSettings.setTypeToMock(Set.class);
        mockSettings.defaultAnswer(new Returns(10));
        Set<?> proxy = mockMaker.createMock(mockSettings, new MockHandlerImpl<Set>(mockSettings));

        assertThat(proxy.size()).isEqualTo(10);
    }

    @Test
    public void should_mock_interface_to_string() {
        MockSettingsImpl<Set> mockSettings = new MockSettingsImpl<Set>();
        mockSettings.setTypeToMock(Set.class);
        mockSettings.defaultAnswer(new Returns("foo"));
        Set<?> proxy = mockMaker.createMock(mockSettings, new MockHandlerImpl<Set>(mockSettings));

        assertThat(proxy.toString()).isEqualTo("foo");
    }

    @Test
    public void should_remove_recursive_self_call_from_stack_trace() throws Exception {
        StackTraceElement[] stack = new StackTraceElement[]{
                new StackTraceElement("foo", "", "", -1),
                new StackTraceElement(SampleInterface.class.getName(), "", "", -1),
                new StackTraceElement("qux", "", "", -1),
                new StackTraceElement("bar", "", "", -1),
                new StackTraceElement("baz", "", "", -1)
        };

        Throwable throwable = new Throwable();
        throwable.setStackTrace(stack);
        throwable = MockMethodAdvice.hideRecursiveCall(throwable, 2, SampleInterface.class);

        assertThat(throwable.getStackTrace()).isEqualTo(new StackTraceElement[]{
                new StackTraceElement("foo", "", "", -1),
                new StackTraceElement("bar", "", "", -1),
                new StackTraceElement("baz", "", "", -1)
        });
    }

    @Test
    public void should_handle_missing_or_inconsistent_stack_trace() throws Exception {
        Throwable throwable = new Throwable();
        throwable.setStackTrace(new StackTraceElement[0]);
        assertThat(MockMethodAdvice.hideRecursiveCall(throwable, 0, SampleInterface.class)).isSameAs(throwable);
    }

    @Test
    public void should_provide_reason_for_wrapper_class() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(Integer.class);
        assertThat(mockable.nonMockableReason()).isEqualTo("Cannot mock wrapper types, String.class or Class.class");
    }

    @Test
    public void should_provide_reason_for_vm_unsupported() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(int[].class);
        assertThat(mockable.nonMockableReason()).isEqualTo("VM does not not support modification of given type");
    }

    @Test
    public void should_mock_method_of_package_private_class() throws Exception {
        MockCreationSettings<NonPackagePrivateSubClass> settings = settingsFor(NonPackagePrivateSubClass.class);
        NonPackagePrivateSubClass proxy = mockMaker.createMock(settings, new MockHandlerImpl<NonPackagePrivateSubClass>(settings));
        assertThat(proxy.value()).isEqualTo("bar");
    }

    @Test
    public void is_type_mockable_excludes_String() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(String.class);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("Cannot mock wrapper types, String.class or Class.class");
    }

    @Test
    public void is_type_mockable_excludes_Class() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(Class.class);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("Cannot mock wrapper types, String.class or Class.class");
    }

    @Test
    public void is_type_mockable_excludes_primitive_classes() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(int.class);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("primitive");
    }

    @Test
    public void is_type_mockable_allows_anonymous() {
        Observer anonymous = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
            }
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
    public void is_type_mockable_give_allow_final_mockable_from_JDK() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(Pattern.class);
        assertThat(mockable.mockable()).isTrue();
        assertThat(mockable.nonMockableReason()).isEqualTo("");
    }

    @Test
    public void test_parameters_retention() throws Exception {
        assumeTrue(ClassFileVersion.ofThisVm().isAtLeast(JAVA_V8));

        Class<?> typeWithParameters = new ByteBuddy()
                .subclass(Object.class)
                .defineMethod("foo", void.class, Visibility.PUBLIC)
                .withParameter(String.class, "bar")
                .intercept(StubMethod.INSTANCE)
                .make()
                .load(null)
                .getLoaded();

        MockCreationSettings<?> settings = settingsFor(typeWithParameters);
        @SuppressWarnings("unchecked")
        Object proxy = mockMaker.createMock(settings, new MockHandlerImpl(settings));

        assertThat(proxy.getClass()).isEqualTo(typeWithParameters);
        assertThat(new TypeDescription.ForLoadedType(typeWithParameters).getDeclaredMethods().filter(named("foo"))
                .getOnly().getParameters().getOnly().getName()).isEqualTo("bar");
    }

    private static <T> MockCreationSettings<T> settingsFor(Class<T> type, Class<?>... extraInterfaces) {
        MockSettingsImpl<T> mockSettings = new MockSettingsImpl<T>();
        mockSettings.setTypeToMock(type);
        mockSettings.defaultAnswer(new Returns("bar"));
        if (extraInterfaces.length > 0) mockSettings.extraInterfaces(extraInterfaces);
        return mockSettings;
    }

    @Test
    public void testMockDispatcherIsRelocated() throws Exception {
        assertThat(InlineByteBuddyMockMaker.class.getClassLoader()
            .getResource("org/mockito/internal/creation/bytebuddy/inject/MockMethodDispatcher.raw"))
            .isNotNull();
    }

    private static final class FinalClass {

        public String foo() {
            return "foo";
        }
    }

    private enum EnumClass {

        INSTANCE;

        public String foo() {
            return "foo";
        }
    }

    private abstract static class FinalMethodAbstractType {

        public final String foo() {
            return "foo";
        }

        public abstract String bar();
    }

    private static class FinalMethod {

        public final String foo() {
            return "foo";
        }
    }

    private interface SampleInterface {

        String bar();
    }

    /*package-private*/ abstract class PackagePrivateSuperClass {

        public abstract String indirect();

        public String value() {
            return indirect() + "qux";
        }
    }

    public class NonPackagePrivateSubClass extends PackagePrivateSuperClass {

        @Override
        public String indirect() {
            return "foo";
        }
    }

    public static class GenericClass<T> {

        public T value() {
            return null;
        }
    }

    public static class GenericSubClass extends GenericClass<String> {
    }
}
