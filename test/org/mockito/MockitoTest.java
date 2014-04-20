/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@SuppressWarnings("unchecked")
public class MockitoTest extends TestBase {

    @Test
    public void should_remove_stubbable_from_progress_after_stubbing() {
        List mock = Mockito.mock(List.class);
        Mockito.when(mock.add("test")).thenReturn(true);
        //TODO Consider to move to separate test
        assertNull(new ThreadSafeMockingProgress().pullOngoingStubbing());
    }
    
    @Test(expected=NotAMockException.class)
    public void should_validate_mock_when_verifying() {
        Mockito.verify("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void should_validate_mock_when_verifying_with_expected_number_of_invocations() {
        Mockito.verify("notMock", times(19));
    }
    
    @Test(expected=NotAMockException.class)
    public void should_validate_mock_when_verifying_no_more_interactions() {
        Mockito.verifyNoMoreInteractions("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void should_validate_mock_when_verifying_zero_interactions() {
        Mockito.verifyZeroInteractions("notMock");
    }
    
    @SuppressWarnings("deprecation")
    @Test(expected=NotAMockException.class)
    public void should_validate_mock_when_stubbing_void() {
        Mockito.stubVoid("notMock");
    }
    
    @Test(expected=NotAMockException.class)
    public void should_validate_mock_when_creating_in_order_object() {
        Mockito.inOrder("notMock");
    }

    @Test
    public void should_starting_mock_settings_contain_default_behavior() {
        //when
        MockSettingsImpl settings = (MockSettingsImpl) Mockito.withSettings();
        
        //then
        assertEquals(Mockito.RETURNS_DEFAULTS, settings.getDefaultAnswer());
    }
    
    //TODO: stack filter does not work very well when it comes to threads?
}