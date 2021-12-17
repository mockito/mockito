/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockito.internal.creation.MockSettingsImpl;

@SuppressWarnings("unchecked")
public class MockitoTest {

    @Test
    public void shouldRemoveStubbableFromProgressAfterStubbing() {
        List mock = Mockito.mock(List.class);
        Mockito.when(mock.add("test")).thenReturn(true);
        // TODO Consider to move to separate test
        assertThat(mockingProgress().pullOngoingStubbing()).isNull();
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldValidateMockWhenVerifying() {
        assertThatThrownBy(
                        () -> {
                            Mockito.verify("notMock");
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContaining(
                        "Argument passed to verify() is of type String and is not a mock!")
                .hasMessageContaining("Make sure you place the parenthesis correctly!");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldValidateMockWhenVerifyingWithExpectedNumberOfInvocations() {
        assertThatThrownBy(
                        () -> {
                            Mockito.verify("notMock", times(19));
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContaining(
                        "Argument passed to verify() is of type String and is not a mock!")
                .hasMessageContaining("Make sure you place the parenthesis correctly!");
    }

    @Test
    public void shouldValidateMockWhenVerifyingNoMoreInteractions() {
        assertThatThrownBy(
                        () -> {
                            Mockito.verifyNoMoreInteractions("notMock");
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContaining("Argument(s) passed is not a mock!");
    }

    @Test
    public void shouldValidateMockWhenVerifyingNoInteractions() {
        assertThatThrownBy(
                        () -> {
                            Mockito.verifyNoInteractions("notMock");
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContaining("Argument(s) passed is not a mock!");
    }

    @Test
    public void shouldValidateNullMockWhenVerifyingNoInteractions() {
        assertThatThrownBy(
                        () -> {
                            Mockito.verifyNoInteractions(new Object[] {null});
                        })
                .isInstanceOf(NullInsteadOfMockException.class)
                .hasMessageContaining("Argument(s) passed is null!");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldValidateMockWhenCreatingInOrderObject() {
        assertThatThrownBy(
                        () -> {
                            Mockito.inOrder("notMock");
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContaining("Argument(s) passed is not a mock!");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldGiveExplanationOnStaticMockingWithoutInlineMockMaker() {
        assertThatThrownBy(
                        () -> {
                            Mockito.mockStatic(Object.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "The used MockMaker SubclassByteBuddyMockMaker does not support the creation of static mocks",
                        "Mockito's inline mock maker supports static mocks based on the Instrumentation API.",
                        "You can simply enable this mock mode, by placing the 'mockito-inline' artifact where you are currently using 'mockito-core'.",
                        "Note that Mockito's inline mock maker is not supported on Android.");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldGiveExplanationOnConstructionMockingWithoutInlineMockMaker() {
        assertThatThrownBy(
                        () -> {
                            Mockito.mockConstruction(Object.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "The used MockMaker SubclassByteBuddyMockMaker does not support the creation of construction mocks",
                        "Mockito's inline mock maker supports construction mocks based on the Instrumentation API.",
                        "You can simply enable this mock mode, by placing the 'mockito-inline' artifact where you are currently using 'mockito-core'.",
                        "Note that Mockito's inline mock maker is not supported on Android.");
    }

    @Test
    public void shouldStartingMockSettingsContainDefaultBehavior() {
        // given
        MockSettingsImpl<?> settings = (MockSettingsImpl<?>) Mockito.withSettings();

        // when / then
        assertThat(settings.getDefaultAnswer()).isEqualTo(Mockito.RETURNS_DEFAULTS);
    }
}
