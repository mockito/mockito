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
import static org.junit.Assert.assertEquals;
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
    public void shouldGiveExplanationOnStaticMockingMockMaker() {
        Assume.assumeThat(Plugins.getMockMaker(), not(instanceOf(InlineMockMaker.class)));

        assertThatThrownBy(
                        () -> {
                            Mockito.mockStatic(Object.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "The used MockMaker SubclassByteBuddyMockMaker does not support the creation of static mocks",
                        "Ensure your MockMaker implementation supports this feature.",
                        "Note that static mocks maker is not supported on Android.");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldGiveExplanationOnConstructionMockingMockMaker() {
        Assume.assumeThat(Plugins.getMockMaker(), not(instanceOf(InlineMockMaker.class)));

        assertThatThrownBy(
                        () -> {
                            Mockito.mockConstruction(Object.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "The used MockMaker SubclassByteBuddyMockMaker does not support the creation of construction mocks",
                        "Ensure your MockMaker implementation supports this feature.",
                        "Note that construction mocks maker is not supported on Android.");
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

    @Test
    public void ensure_reified_mocked_static_can_be_called_without_parameters() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));

        try (MockedStatic<Dummy> mockedStatic = Mockito.mockStatic()) {
            mockedStatic.when(Dummy::getValue).thenReturn("stub");
            assertEquals("stub", Dummy.getValue());
        }
    }

    @Test
    public void ensure_reified_mocked_static_can_be_called_with_default_answer() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));

        try (MockedStatic<Dummy> ignored = Mockito.mockStatic(Answers.CALLS_REAL_METHODS)) {
            assertEquals("value", Dummy.getValue());
        }
    }

    @Test
    public void ensure_reified_mocked_static_can_be_called_with_name() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));

        try (MockedStatic<Dummy> mockedStatic = Mockito.mockStatic("name")) {
            assertThatThrownBy(() -> mockedStatic.verify(Dummy::getValue))
                    .hasMessageContaining("name.getValue()");
        }
    }

    @Test
    public void ensure_reified_mocked_static_can_be_called_with_settings() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        MockSettingsImpl<Dummy> settings = (MockSettingsImpl<Dummy>) Mockito.withSettings();

        try (MockedStatic<Dummy> mockedStatic = Mockito.mockStatic(settings)) {
            mockedStatic.when(Dummy::getValue).thenReturn("stub");
            assertEquals("stub", Dummy.getValue());
        }
    }

    @Test
    @SuppressWarnings({"resource", "DataFlowIssue"})
    public void ensure_reified_mocked_static_should_not_be_called_with_null() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        MockSettingsImpl<Dummy> settings = (MockSettingsImpl<Dummy>) Mockito.withSettings();

        assertThatThrownBy(() -> Mockito.mockStatic(settings, (Dummy[]) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Please don't pass any values here");
    }

    @Test
    @SuppressWarnings({"resource"})
    public void ensure_reified_mocked_static_should_not_be_called_with_parameters() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        MockSettingsImpl<Dummy> settings = (MockSettingsImpl<Dummy>) Mockito.withSettings();

        assertThatThrownBy(() -> Mockito.mockStatic(settings, new Dummy[] {new Dummy()}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Please don't pass any values here");
    }

    @Test
    public void ensure_reified_mocked_construction_can_be_called_without_parameters() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));

        try (MockedConstruction<Dummy> mockedConstruction = Mockito.mockConstruction()) {
            final Dummy dummy = new Dummy();
            final Dummy constructed = mockedConstruction.constructed().get(0);

            Mockito.when(constructed.getAnswer()).thenReturn(1);
            assertEquals(1, dummy.getAnswer());
        }
    }

    @Test
    public void ensure_reified_mocked_construction_can_be_called_with_initializer() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));

        try (MockedConstruction<Dummy> ignored =
                Mockito.mockConstruction(
                        (mock, context) -> Mockito.when(mock.getAnswer()).thenReturn(1))) {
            final Dummy dummy = new Dummy();

            assertEquals(1, dummy.getAnswer());
        }
    }

    @Test
    public void ensure_reified_mocked_construction_can_be_called_with_settings() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        MockSettingsImpl<Dummy> settings = (MockSettingsImpl<Dummy>) Mockito.withSettings();

        try (MockedConstruction<Dummy> mockedConstruction = Mockito.mockConstruction(settings)) {
            final Dummy dummy = new Dummy();
            final Dummy constructed = mockedConstruction.constructed().get(0);

            Mockito.when(constructed.getAnswer()).thenReturn(1);
            assertEquals(1, dummy.getAnswer());
        }
    }

    @Test
    public void ensure_reified_mocked_construction_can_be_called_with_settings_factory() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        MockSettingsImpl<Dummy> settings = (MockSettingsImpl<Dummy>) Mockito.withSettings();

        try (MockedConstruction<Dummy> mockedConstruction =
                Mockito.mockConstruction(context -> settings)) {
            final Dummy dummy = new Dummy();
            final Dummy constructed = mockedConstruction.constructed().get(0);

            Mockito.when(constructed.getAnswer()).thenReturn(1);
            assertEquals(1, dummy.getAnswer());
        }
    }

    @Test
    public void ensure_reified_mocked_construction_can_be_called_with_settings_and_initializer() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        MockSettingsImpl<Dummy> settings = (MockSettingsImpl<Dummy>) Mockito.withSettings();

        try (MockedConstruction<Dummy> ignored =
                Mockito.mockConstruction(
                        settings,
                        (mock, context) -> Mockito.when(mock.getAnswer()).thenReturn(1))) {
            final Dummy dummy = new Dummy();

            assertEquals(1, dummy.getAnswer());
        }
    }

    @Test
    public void ensure_reified_mocked_construction_can_be_called_with_factory_and_initializer() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        MockSettingsImpl<Dummy> settings = (MockSettingsImpl<Dummy>) Mockito.withSettings();

        try (MockedConstruction<Dummy> ignored =
                Mockito.mockConstruction(
                        context -> settings,
                        (mock, context) -> Mockito.when(mock.getAnswer()).thenReturn(1))) {
            final Dummy dummy = new Dummy();

            assertEquals(1, dummy.getAnswer());
        }
    }

    @Test
    @SuppressWarnings({"resource", "DataFlowIssue"})
    public void ensure_reified_mocked_construction_should_not_be_called_with_null() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        MockSettingsImpl<Dummy> settings = (MockSettingsImpl<Dummy>) Mockito.withSettings();

        assertThatThrownBy(
                        () ->
                                Mockito.mockConstruction(
                                        context -> settings,
                                        (mock, context) ->
                                                Mockito.when(mock.getAnswer()).thenReturn(1),
                                        (Dummy[]) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Please don't pass any values here");
    }

    @Test
    @SuppressWarnings({"resource"})
    public void ensure_reified_mocked_construction_should_not_be_called_with_parameters() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        MockSettingsImpl<Dummy> settings = (MockSettingsImpl<Dummy>) Mockito.withSettings();

        assertThatThrownBy(
                        () ->
                                Mockito.mockConstruction(
                                        context -> settings,
                                        (mock, context) ->
                                                Mockito.when(mock.getAnswer()).thenReturn(1),
                                        new Dummy[] {new Dummy()}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Please don't pass any values here");
    }

    private static final class Dummy {

        public static String getValue() {
            return "value";
        }

        public int getAnswer() {
            return 42;
        }
    }
}
