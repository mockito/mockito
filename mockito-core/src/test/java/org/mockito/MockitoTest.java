/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.times;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.util.List;

import org.junit.Assume;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.listeners.InvocationListener;
import org.mockito.plugins.InlineMockMaker;

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
        Assume.assumeThat(Plugins.getMockMaker(), not(instanceOf(InlineMockMaker.class)));

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
        Assume.assumeThat(Plugins.getMockMaker(), not(instanceOf(InlineMockMaker.class)));

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

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldGiveExplanationOnConstructionMockingWithInlineMockMaker() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));

        assertThatThrownBy(
                        () -> {
                            Mockito.mockConstruction(Object.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "It is not possible to mock construction of the Object class to avoid inference with default object constructor chains");
    }

    @Test
    public void shouldStartingMockSettingsContainDefaultBehavior() {
        // given
        MockSettingsImpl<?> settings = (MockSettingsImpl<?>) Mockito.withSettings();

        // when / then
        assertThat(settings.getDefaultAnswer()).isEqualTo(Mockito.RETURNS_DEFAULTS);
    }

    @Test
    @SuppressWarnings({"DoNotMock", "DoNotMockAutoValue"})
    public void automaticallyDetectsClassToMock() {
        List<String> mock = Mockito.mock();
        Mockito.when(mock.size()).thenReturn(42);
        assertThat(mock.size()).isEqualTo(42);
    }

    @Test
    @SuppressWarnings({"DoNotMock", "DoNotMockAutoValue"})
    public void newMockMethod_shouldNotBeCalledWithNullParameters() {
        assertThatThrownBy(
                        () -> {
                            Mockito.mock((Object[]) null);
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Please don't pass any values here");
    }

    @Test
    public void reifiedMockMethodWithNameSetsTheExpectedName() {
        List<String> mock = Mockito.mock("My super cool new mock");
        assertThat(mock).hasToString("My super cool new mock");
    }

    @Test
    public void reifiedMockMethodWithDefaultAnswerSetsTheDefaultAnswer() {
        abstract class Something {
            abstract Something somethingElse();
        }

        Something something = Mockito.mock(Answers.RETURNS_SELF);

        assertThat(something.somethingElse()).isSameAs(something);
    }

    @Test
    public void reifiedMockMethodWithSettingsAppliesTheSettings() {

        InvocationListener invocationListener = Mockito.mock(InvocationListener.class);

        List<Object> mock =
                Mockito.mock(
                        Mockito.withSettings()
                                .name("my name here")
                                .invocationListeners(invocationListener));

        assertThat(mock).hasToString("my name here");
        Mockito.verify(invocationListener).reportInvocation(ArgumentMatchers.any());
    }

    @Test
    @SuppressWarnings({"DoNotMock", "DoNotMockAutoValue"})
    public void newMockMethod_shouldNotBeCalledWithParameters() {
        assertThatThrownBy(
                        () -> {
                            Mockito.mock(asList("1", "2"), asList("3", "4"));
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Please don't pass any values here");
    }

    @Test
    @SuppressWarnings({"DoNotMock", "DoNotMockAutoValue"})
    public void automaticallyDetectsClassToSpy() {
        List<String> mock = Mockito.spy();
        Mockito.when(mock.size()).thenReturn(42);
        assertThat(mock.size()).isEqualTo(42);
        assertThat(mock.get(0)).isNull();
    }

    @Test
    @SuppressWarnings({"DoNotMock", "DoNotMockAutoValue"})
    public void newSpyMethod_shouldNotBeCalledWithParameters() {
        assertThatThrownBy(
                        () -> {
                            Mockito.spy(asList("1", "2"), asList("3", "4"));
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Please don't pass any values here");
    }
}
