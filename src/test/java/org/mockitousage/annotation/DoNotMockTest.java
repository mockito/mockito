/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.DoNotMock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.DoNotMockException;

public class DoNotMockTest {

    @Test
    public void can_not_mock_class_annotated_with_donotmock() {
        assertThatThrownBy(
                        () -> {
                            NotMockable notMockable = mock(NotMockable.class);
                        })
                .isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void can_not_mock_class_via_mock_annotation() {
        assertThatThrownBy(
                        () -> {
                            MockitoAnnotations.openMocks(new TestClass());
                        })
                .isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void can_not_mock_class_with_interface_annotated_with_donotmock() {
        assertThatThrownBy(
                        () -> {
                            SubclassOfNotMockableInterface notMockable =
                                    mock(SubclassOfNotMockableInterface.class);
                        })
                .isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void can_not_mock_subclass_with_unmockable_interface() {
        assertThatThrownBy(
                        () -> {
                            DoubleSubclassOfInterface notMockable =
                                    mock(DoubleSubclassOfInterface.class);
                        })
                .isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void can_not_mock_subclass_with_unmockable_superclass() {
        assertThatThrownBy(
                        () -> {
                            SubclassOfNotMockableSuperclass notMockable =
                                    mock(SubclassOfNotMockableSuperclass.class);
                        })
                .isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void
            can_not_mock_subclass_with_unmockable_interface_that_extends_non_mockable_interface() {
        assertThatThrownBy(
                        () -> {
                            SubclassOfSubInterfaceOfNotMockableInterface notMockable =
                                    mock(SubclassOfSubInterfaceOfNotMockableInterface.class);
                        })
                .isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void thrown_exception_includes_non_mockable_reason() {
        assertThatThrownBy(
                        () -> {
                            NotMockable notMockable = mock(NotMockable.class);
                        })
                .isInstanceOf(DoNotMockException.class)
                .hasMessageContaining("Create a real instance instead");
    }

    @Test
    public void thrown_exception_includes_special_non_mockable_reason() {
        assertThatThrownBy(
                        () -> {
                            NotMockableWithReason notMockable = mock(NotMockableWithReason.class);
                        })
                .isInstanceOf(DoNotMockException.class)
                .hasMessageContaining("Special reason");
    }

    @Test
    public void can_not_mock_class_with_custom_donotmock_annotation() {
        assertThatThrownBy(
                        () -> {
                            NotMockableWithDifferentAnnotation notMockable =
                                    mock(NotMockableWithDifferentAnnotation.class);
                        })
                .isInstanceOf(DoNotMockException.class);
    }

    @DoNotMock
    private static class NotMockable {}

    @DoNotMock
    private interface NotMockableInterface {}

    @org.mockitousage.annotation.org.mockito.DoNotMock
    private static class NotMockableWithDifferentAnnotation {}

    @DoNotMock(reason = "Special reason")
    private interface NotMockableWithReason {}

    static class SubclassOfNotMockableInterface implements NotMockableInterface {}

    private static class DoubleSubclassOfInterface extends SubclassOfNotMockableInterface {}

    private static class SubclassOfNotMockableSuperclass extends NotMockable {}

    private interface SubInterfaceOfNotMockableInterface extends NotMockableInterface {}

    private static class SubclassOfSubInterfaceOfNotMockableInterface
            implements SubInterfaceOfNotMockableInterface {}

    private static class TestClass {
        @Mock private NotMockable notMockable;
    }
}
