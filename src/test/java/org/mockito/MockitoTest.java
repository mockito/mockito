/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;

@SuppressWarnings("unchecked")
public class MockitoTest {

    @Test
    public void shouldRemoveStubbableFromProgressAfterStubbing() {
        List mock = Mockito.mock(List.class);
        Mockito.when(mock.add("test")).thenReturn(true);
        //TODO Consider to move to separate test
        assertThat(mockingProgress().pullOngoingStubbing()).isNull();
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifying() {
        Mockito.verify("notMock");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingWithExpectedNumberOfInvocations() {
        Mockito.verify("notMock", times(19));
    }

    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions("notMock");
    }

    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenVerifyingZeroInteractions() {
        Mockito.verifyZeroInteractions("notMock");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test(expected=NotAMockException.class)
    public void shouldValidateMockWhenCreatingInOrderObject() {
        Mockito.inOrder("notMock");
    }

    @Test
    public void shouldStartingMockSettingsContainDefaultBehavior() {
        //when
        MockSettingsImpl<?> settings = (MockSettingsImpl<?>) Mockito.withSettings();

        //then
        assertThat(Mockito.RETURNS_DEFAULTS).isEqualTo(settings.getDefaultAnswer());
    }

    @Test
    public void shouldProduceDifferentNumbers() {
        Random random = new Random();
        List<String> stringList = Mockito.mock(List.class);

        SerializableSupplier<Integer> supplier = () -> random.nextInt(100);
        when(stringList.size()).thenReturn(supplier, Integer.class);

        for(int i = 0; i < 10; i ++) {
            assertThat(stringList.size()).isBetween(0, 100);
        }
    }

    @Test
    public void shouldProduceDifferentNumbers_mockitoStubber() {
        Random random = new Random();
        List<String> stringList = Mockito.mock(List.class);

        SerializableSupplier<Integer> supplier = () -> random.nextInt(100);
        Mockito.doReturn(supplier, Integer.class).when(stringList).size();

        for(int i = 0; i < 10; i ++) {
            assertThat(stringList.size()).isBetween(0, 100);
        }
    }

}
