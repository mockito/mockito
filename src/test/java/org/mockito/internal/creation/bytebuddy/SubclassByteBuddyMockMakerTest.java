/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.plugins.MockMaker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Observable;
import java.util.Observer;

import static org.assertj.core.api.Assertions.assertThat;

public class SubclassByteBuddyMockMakerTest extends AbstractByteBuddyMockMakerTest<SubclassByteBuddyMockMaker> {

    public SubclassByteBuddyMockMakerTest() {
        super(new SubclassByteBuddyMockMaker());
    }

    @Test
    public void is_type_mockable_excludes_primitive_wrapper_classes() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(Integer.class);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("final");
    }

    @Test
    public void is_type_mockable_excludes_primitive_classes() {
        MockMaker.TypeMockability mockable = mockMaker.isTypeMockable(int.class);
        assertThat(mockable.mockable()).isFalse();
        assertThat(mockable.nonMockableReason()).contains("primitive");
    }

    @Test
    public void is_type_mockable_allow_anonymous() {
        Observer anonymous = new Observer() {
            @Override public void update(Observable o, Object arg) { }
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
    public void mock_type_with_annotations() throws Exception {
        MockSettingsImpl<ClassWithAnnotation> mockSettings = new MockSettingsImpl<ClassWithAnnotation>();
        mockSettings.setTypeToMock(ClassWithAnnotation.class);

        ClassWithAnnotation proxy = mockMaker.createMock(mockSettings, dummyHandler());

        assertThat(proxy.getClass().isAnnotationPresent(SampleAnnotation.class)).isTrue();
        assertThat(proxy.getClass().getAnnotation(SampleAnnotation.class).value()).isEqualTo("foo");

        assertThat(proxy.getClass().getMethod("sampleMethod").isAnnotationPresent(SampleAnnotation.class)).isTrue();
        assertThat(proxy.getClass().getMethod("sampleMethod").getAnnotation(SampleAnnotation.class).value()).isEqualTo("bar");
    }

    @Test
    public void mock_type_without_annotations() throws Exception {
        MockSettingsImpl<ClassWithAnnotation> mockSettings = new MockSettingsImpl<ClassWithAnnotation>();
        mockSettings.setTypeToMock(ClassWithAnnotation.class);
        mockSettings.withoutAnnotations();

        ClassWithAnnotation proxy = mockMaker.createMock(mockSettings, dummyHandler());

        assertThat(proxy.getClass().isAnnotationPresent(SampleAnnotation.class)).isFalse();
        assertThat(proxy.getClass().getMethod("sampleMethod").isAnnotationPresent(SampleAnnotation.class)).isFalse();
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
}
