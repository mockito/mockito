/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.debugging.LogInvocationsToStdOutListener;
import org.mockito.invocation.InvocationListener;
import org.mockitoutil.TestBase;

public class MockSettingsImplTest extends TestBase {

    private MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    
    private static final InvocationListener SOME_LISTENER = mock(InvocationListener.class);

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
        assertEquals(List.class, mockSettingsImpl.getExtraInterfaces()[0]);
        assertEquals(Set.class, mockSettingsImpl.getExtraInterfaces()[1]);
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
        assertTrue(mockSettingsImpl.getInvocationListener().isEmpty());

        //when
        mockSettingsImpl.verboseLogging();

        //then
        assertEquals(1, mockSettingsImpl.getInvocationListener().size());
        assertTrue(getListener(mockSettingsImpl) instanceof LogInvocationsToStdOutListener);
    }

    @Test
    public void shouldAddVerboseLoggingListenerOnlyOnce() {
    	//given
    	assertTrue(mockSettingsImpl.getInvocationListener().isEmpty());
    	
    	//when
    	mockSettingsImpl.verboseLogging().verboseLogging();
    	
    	//then
    	assertEquals(1, mockSettingsImpl.getInvocationListener().size());
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowNullListener() {
    	mockSettingsImpl.callback(null);
    }

    @Test
    public void shouldAddInvocationListener() {
    	//given
    	assertTrue(mockSettingsImpl.getInvocationListener().isEmpty());
    	
    	//when
    	mockSettingsImpl.callback(SOME_LISTENER);
    	
    	//then
    	assertEquals(1, mockSettingsImpl.getInvocationListener().size());
    	assertSame(SOME_LISTENER, getListener(mockSettingsImpl));
    }
    
    @Test
    public void shouldAddInvocationListenerOnlyOnce() {
    	//given
    	assertTrue(mockSettingsImpl.getInvocationListener().isEmpty());
    	
    	//when
    	mockSettingsImpl.callback(SOME_LISTENER).callback(SOME_LISTENER);
    	
    	//then
    	assertEquals(1, mockSettingsImpl.getInvocationListener().size());
    }
    
    private InvocationListener getListener(MockSettingsImpl settings) {
    	return settings.getInvocationListener().iterator().next();
    }
    
}