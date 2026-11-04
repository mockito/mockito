/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.withSettings;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;
import org.mockito.DoNotMock;
import org.mockito.MockedStatic;
import org.mockito.exceptions.misusing.DoNotMockException;

public class DoNotMockMessageTest {

    private static final String CUSTOM_REASON = "Use ConcreteFake instead";
    private static final String CUSTOM_VALUE = "custom-policy-3592";

    @Test
    public void official_default_message_is_preserved() {
        assertThatThrownBy(() -> mock(OfficialDefault.class))
                .isInstanceOf(DoNotMockException.class)
                .hasMessage(
                        OfficialDefault.class
                                + " is annotated with @org.mockito.DoNotMock and can't be mocked."
                                + " Create a real instance instead.");
    }

    @Test
    public void official_reason_message_is_preserved() {
        assertThatThrownBy(() -> mock(OfficialWithReason.class))
                .isInstanceOf(DoNotMockException.class)
                .hasMessage(
                        OfficialWithReason.class
                                + " is annotated with @org.mockito.DoNotMock and can't be mocked."
                                + " Use the official fake instead");
    }

    @Test
    public void official_reason_message_is_preserved_for_static_mocking() {
        assertThatThrownBy(
                        () -> {
                            try (MockedStatic<OfficialWithReason> ignored =
                                    mockStatic(OfficialWithReason.class)) {}
                        })
                .isInstanceOf(DoNotMockException.class)
                .hasMessage(
                        OfficialWithReason.class
                                + " is annotated with @org.mockito.DoNotMock and can't be mocked."
                                + " Use the official fake instead");
    }

    @Test
    public void custom_annotation_attributes_are_included() {
        assertCustomMessage(() -> mock(CustomType.class), CustomType.class);
    }

    @Test
    public void custom_annotation_attributes_are_included_for_static_mocking() {
        assertCustomMessage(
                () -> {
                    try (MockedStatic<CustomType> ignored = mockStatic(CustomType.class)) {}
                },
                CustomType.class);
    }

    @Test
    public void custom_annotation_attributes_on_superclass_are_included() {
        assertCustomMessage(() -> mock(CustomSubclass.class), CustomType.class);
    }

    @Test
    public void custom_annotation_attributes_on_interface_are_included() {
        assertCustomMessage(() -> mock(CustomImplementation.class), CustomInterface.class);
    }

    @Test
    public void custom_annotation_attributes_on_inherited_interface_are_included() {
        assertCustomMessage(() -> mock(InheritedCustomImplementation.class), CustomInterface.class);
    }

    @Test
    public void custom_annotation_attributes_on_extra_interface_are_included() {
        assertCustomMessage(
                () -> mock(PlainType.class, withSettings().extraInterfaces(CustomInterface.class)),
                CustomInterface.class);
    }

    @Test
    public void custom_annotation_without_attributes_is_included() {
        assertThatThrownBy(() -> mock(EmptyCustomType.class))
                .isInstanceOf(DoNotMockException.class)
                .hasMessageContaining(EmptyCustomType.class.getName())
                .hasMessageContaining(
                        EmptyCustomType.class
                                .getAnnotation(
                                        org.mockitousage.annotation.org.mockito.DoNotMock.class)
                                .toString())
                .hasMessageNotContaining("@@");
    }

    @Test
    public void unannotated_type_can_be_mocked() {
        assertThat(mock(PlainType.class)).isNotNull();
    }

    @Test
    public void unrelated_annotation_does_not_prevent_mocking() {
        assertThat(mock(UnrelatedType.class)).isNotNull();
    }

    private void assertCustomMessage(ThrowingCallable action, Class<?> annotatedType) {
        assertThatThrownBy(action)
                .isInstanceOf(DoNotMockException.class)
                .hasMessageContaining(annotatedType.getName())
                .hasMessageContaining(CUSTOM_REASON)
                .hasMessageContaining(CUSTOM_VALUE)
                .hasMessageContaining(
                        "@"
                                + org.mockitousage.annotation.custom.org.mockito.DoNotMock.class
                                        .getName())
                .hasMessageNotContaining("@@");
    }

    @DoNotMock
    private static class OfficialDefault {}

    @DoNotMock(reason = "Use the official fake instead")
    private interface OfficialWithReason {}

    @org.mockitousage.annotation.custom.org.mockito.DoNotMock(
            reason = CUSTOM_REASON,
            value = CUSTOM_VALUE)
    private static class CustomType {}

    private static class CustomSubclass extends CustomType {}

    @org.mockitousage.annotation.custom.org.mockito.DoNotMock(
            reason = CUSTOM_REASON,
            value = CUSTOM_VALUE)
    private interface CustomInterface {}

    private static class CustomImplementation implements CustomInterface {}

    private interface CustomSubInterface extends CustomInterface {}

    private static class InheritedCustomImplementation implements CustomSubInterface {}

    @org.mockitousage.annotation.org.mockito.DoNotMock
    private static class EmptyCustomType {}

    private static class PlainType {}

    @UnrelatedAnnotation
    private static class UnrelatedType {}

    @Retention(RUNTIME)
    @Target(TYPE)
    private @interface UnrelatedAnnotation {}
}
