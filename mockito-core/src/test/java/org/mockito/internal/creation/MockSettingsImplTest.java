/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.StubbingLookupListener;
import org.mockitoutil.TestBase;

public class MockSettingsImplTest extends TestBase {

    private MockSettingsImpl<?> mockSettingsImpl = new MockSettingsImpl<Object>();

    @Mock private InvocationListener invocationListener;
    @Mock private StubbingLookupListener stubbingLookupListener;

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotAllowSettingNullInterface() {
        assertThatThrownBy(
                        () -> {
                            mockSettingsImpl.extraInterfaces(List.class, null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("extraInterfaces() does not accept null parameters.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotAllowNonInterfaces() {
        assertThatThrownBy(
                        () -> {
                            mockSettingsImpl.extraInterfaces(List.class, LinkedList.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "extraInterfaces() accepts only interfaces",
                        "You passed following type: LinkedList which is not an interface.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotAllowUsingTheSameInterfaceAsExtra() {
        assertThatThrownBy(
                        () -> {
                            mockSettingsImpl.extraInterfaces(List.class, LinkedList.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "extraInterfaces() accepts only interfaces.",
                        "You passed following type: LinkedList which is not an interface.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotAllowEmptyExtraInterfaces() {
        assertThatThrownBy(
                        () -> {
                            mockSettingsImpl.extraInterfaces();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("extraInterfaces() requires at least one interface.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldNotAllowNullArrayOfExtraInterfaces() {
        assertThatThrownBy(
                        () -> {
                            mockSettingsImpl.extraInterfaces((Class<?>[]) null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("extraInterfaces() requires at least one interface.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldAllowMultipleInterfaces() {
        // when
        mockSettingsImpl.extraInterfaces(List.class, Set.class);

        // then
        assertThat(mockSettingsImpl.getExtraInterfaces().size()).isEqualTo(2);
        assertThat(mockSettingsImpl.getExtraInterfaces()).contains(List.class);
        assertThat(mockSettingsImpl.getExtraInterfaces()).contains(Set.class);
    }

    @Test
    public void shouldSetMockToBeSerializable() {
        // when
        mockSettingsImpl.serializable();

        // then
        assertThat(mockSettingsImpl.isSerializable()).isTrue();
    }

    @Test
    public void shouldKnowIfIsSerializable() {
        // given
        assertThat(mockSettingsImpl.isSerializable()).isFalse();

        // when
        mockSettingsImpl.serializable();

        // then
        assertThat(mockSettingsImpl.isSerializable()).isTrue();
    }

    @Test
    public void shouldAddVerboseLoggingListener() {
        // given
        assertThat(mockSettingsImpl.hasInvocationListeners()).isFalse();

        // when
        mockSettingsImpl.verboseLogging();

        // then
        assertThat(mockSettingsImpl.getInvocationListeners())
                .extracting("class")
                .contains(VerboseMockInvocationLogger.class);
    }

    @Test
    public void shouldAddVerboseLoggingListenerOnlyOnce() {
        // given
        assertThat(mockSettingsImpl.hasInvocationListeners()).isFalse();

        // when
        mockSettingsImpl.verboseLogging().verboseLogging();

        // then
        assertThat(mockSettingsImpl.getInvocationListeners()).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldAddInvocationListener() {
        // given
        assertThat(mockSettingsImpl.hasInvocationListeners()).isFalse();

        // when
        mockSettingsImpl.invocationListeners(invocationListener);

        // then
        assertThat(mockSettingsImpl.getInvocationListeners()).contains(invocationListener);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canAddDuplicateInvocationListeners_ItsNotOurBusinessThere() {
        // given
        assertThat(mockSettingsImpl.hasInvocationListeners()).isFalse();

        // when
        mockSettingsImpl
                .invocationListeners(invocationListener, invocationListener)
                .invocationListeners(invocationListener);

        // then
        assertThat(mockSettingsImpl.getInvocationListeners())
                .containsSequence(invocationListener, invocationListener, invocationListener);
    }

    @Test
    public void validates_listeners() {
        assertThatThrownBy(
                        () ->
                                mockSettingsImpl.addListeners(
                                        new Object[] {}, new LinkedList<Object>(), "myListeners"))
                .hasMessageContaining("myListeners() requires at least one listener");

        assertThatThrownBy(
                        () ->
                                mockSettingsImpl.addListeners(
                                        null, new LinkedList<Object>(), "myListeners"))
                .hasMessageContaining("myListeners() does not accept null vararg array");

        assertThatThrownBy(
                        () ->
                                mockSettingsImpl.addListeners(
                                        new Object[] {null},
                                        new LinkedList<Object>(),
                                        "myListeners"))
                .hasMessageContaining("myListeners() does not accept null listeners");
    }

    @Test
    public void validates_stubbing_lookup_listeners() {
        assertThatThrownBy(
                        () ->
                                mockSettingsImpl.stubbingLookupListeners(
                                        new StubbingLookupListener[] {}))
                .hasMessageContaining("stubbingLookupListeners() requires at least one listener");

        assertThatThrownBy(() -> mockSettingsImpl.stubbingLookupListeners(null))
                .hasMessageContaining(
                        "stubbingLookupListeners() does not accept null vararg array");

        assertThatThrownBy(
                        () ->
                                mockSettingsImpl.stubbingLookupListeners(
                                        new StubbingLookupListener[] {null}))
                .hasMessageContaining("stubbingLookupListeners() does not accept null listeners");
    }

    @Test
    public void validates_invocation_listeners() {
        assertThatThrownBy(() -> mockSettingsImpl.invocationListeners(new InvocationListener[] {}))
                .hasMessageContaining("invocationListeners() requires at least one listener");

        assertThatThrownBy(() -> mockSettingsImpl.invocationListeners(null))
                .hasMessageContaining("invocationListeners() does not accept null vararg array");

        assertThatThrownBy(
                        () -> mockSettingsImpl.invocationListeners(new InvocationListener[] {null}))
                .hasMessageContaining("invocationListeners() does not accept null listeners");
    }

    @Test
    public void addListeners_has_empty_listeners_by_default() {
        assertThat(mockSettingsImpl.getInvocationListeners()).isEmpty();
        assertThat(mockSettingsImpl.getStubbingLookupListeners()).isEmpty();
    }

    @Test
    public void addListeners_shouldAddMockObjectListeners() {
        // when
        mockSettingsImpl.invocationListeners(invocationListener);
        mockSettingsImpl.stubbingLookupListeners(stubbingLookupListener);

        // then
        assertThat(mockSettingsImpl.getInvocationListeners()).contains(invocationListener);
        assertThat(mockSettingsImpl.getStubbingLookupListeners()).contains(stubbingLookupListener);
    }

    @Test
    public void addListeners_canAddDuplicateMockObjectListeners_ItsNotOurBusinessThere() {
        // when
        mockSettingsImpl
                .stubbingLookupListeners(stubbingLookupListener)
                .stubbingLookupListeners(stubbingLookupListener)
                .invocationListeners(invocationListener)
                .invocationListeners(invocationListener);

        // then
        assertThat(mockSettingsImpl.getInvocationListeners())
                .containsSequence(invocationListener, invocationListener);
        assertThat(mockSettingsImpl.getStubbingLookupListeners())
                .containsSequence(stubbingLookupListener, stubbingLookupListener);
    }

    @Test
    public void validates_strictness() {
        assertThatThrownBy(() -> mockSettingsImpl.strictness(null))
                .hasMessageContaining("strictness() does not accept null parameter");
    }
}
