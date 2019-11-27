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
        assertThatThrownBy(() -> {
            NotMockable notMockable = mock(NotMockable.class);
        }).isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void can_not_mock_class_via_mock_annotation() {
        assertThatThrownBy(() -> {
            MockitoAnnotations.initMocks(new TestClass());
        }).isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void can_not_mock_class_with_interface_annotated_with_donotmock() {
        assertThatThrownBy(() -> {
            SubclassOfNotMockableInterface notMockable = mock(SubclassOfNotMockableInterface.class);
        }).isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void can_not_mock_subclass_with_unmockable_interface() {
        assertThatThrownBy(() -> {
            DoubleSubclassOfInterface notMockable = mock(DoubleSubclassOfInterface.class);
        }).isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void can_not_mock_subclass_with_unmockable_superclass() {
        assertThatThrownBy(() -> {
            SubclassOfNotMockableSuperclass notMockable = mock(SubclassOfNotMockableSuperclass.class);
        }).isInstanceOf(DoNotMockException.class);
    }

    @DoNotMock
    private static class NotMockable {

    }

    @DoNotMock
    private interface NotMockableInterface {

    }

    static class SubclassOfNotMockableInterface implements NotMockableInterface {}

    private static class DoubleSubclassOfInterface extends SubclassOfNotMockableInterface {}

    private static class SubclassOfNotMockableSuperclass extends NotMockable {}

    private static class TestClass {
        @Mock private NotMockable notMockable;
    }

}
