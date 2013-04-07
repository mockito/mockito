/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.listeners.InvocationListener;
import org.mockitoutil.TestBase;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MockSettingsImplTest extends TestBase {

    private MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    
    @Mock private InvocationListener invocationListener;

    @Test(expected=MockitoException.class)
    public void shouldNotAllowSettingNullInterface() {
        mockSettingsImpl.extraInterfaces(List.class, null);
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowNonInterfaces() {
        mockSettingsImpl.extraInterfaces(List.class, LinkedList.class);
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowUsingTheSameInterfaceAsExtra() {
        mockSettingsImpl.extraInterfaces(List.class, LinkedList.class);
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowEmptyExtraInterfaces() {
        mockSettingsImpl.extraInterfaces();
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowNullArrayOfExtraInterfaces() {
        mockSettingsImpl.extraInterfaces((Class[]) null);
    }
    
    @Test
    public void shouldAllowMultipleInterfaces() {
        //when
        mockSettingsImpl.extraInterfaces(List.class, Set.class);
        
        //then
        assertEquals(2, mockSettingsImpl.getExtraInterfaces().size());
        assertTrue(mockSettingsImpl.getExtraInterfaces().contains(List.class));
        assertTrue(mockSettingsImpl.getExtraInterfaces().contains(Set.class));
    }

    @Test
    public void shouldSetMockToBeSerializable() throws Exception {
        //when
        mockSettingsImpl.serializable();

        //then
        assertTrue(mockSettingsImpl.isSerializable());
    }

    @Test
    public void shouldKnowIfIsSerializable() throws Exception {
        //given
        assertFalse(mockSettingsImpl.isSerializable());

        //when
        mockSettingsImpl.serializable();

        //then
        assertTrue(mockSettingsImpl.isSerializable());
    }

    @Test
    public void shouldAddVerboseLoggingListener() {
        //given
        assertFalse(mockSettingsImpl.hasInvocationListeners());

        //when
        mockSettingsImpl.verboseLogging();

        //then
        assertContainsType(mockSettingsImpl.getInvocationListeners(), VerboseMockInvocationLogger.class);
    }

    @Test
    public void shouldAddVerboseLoggingListenerOnlyOnce() {
    	//given
    	assertFalse(mockSettingsImpl.hasInvocationListeners());
    	
    	//when
    	mockSettingsImpl.verboseLogging().verboseLogging();
    	
    	//then
    	Assertions.assertThat(mockSettingsImpl.getInvocationListeners()).hasSize(1);
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowNullListener() {
    	mockSettingsImpl.invocationListeners(null);
    }

    @Test
    public void shouldAddInvocationListener() {
    	//given
    	assertFalse(mockSettingsImpl.hasInvocationListeners());
    	
    	//when
    	mockSettingsImpl.invocationListeners(invocationListener);
    	
    	//then
        Assertions.assertThat(mockSettingsImpl.getInvocationListeners()).contains(invocationListener);
    }
    
    @Test
    public void canAddDuplicateInvocationListeners_ItsNotOurBusinessThere() {
    	//given
    	assertFalse(mockSettingsImpl.hasInvocationListeners());
    	
    	//when
    	mockSettingsImpl.invocationListeners(invocationListener, invocationListener).invocationListeners(invocationListener);
    	
    	//then
    	Assertions.assertThat(mockSettingsImpl.getInvocationListeners()).containsSequence(invocationListener, invocationListener, invocationListener);
    }

    @Test
    public void shouldReportErrorWhenAddingNoInvocationListeners() throws Exception {
        try {
            mockSettingsImpl.invocationListeners();
            fail();
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).contains("at least one listener");
        }
    }

    @Test
    public void shouldReportErrorWhenAddingANullInvocationListener() throws Exception {
        try {
            mockSettingsImpl.invocationListeners(invocationListener, null);
            fail();
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).contains("does not accept null");
        }
    }
}
