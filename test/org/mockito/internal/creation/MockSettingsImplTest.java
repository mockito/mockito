package org.mockito.internal.creation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MockSettingsImplTest extends TestBase {

    private MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();

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
    public void shouldKnowIfIsASpy() throws Exception {
        //when
        mockSettingsImpl.spiedInstance(new Object());
        
        //then
        assertTrue(mockSettingsImpl.isSpy());
    }
    
    @Test
    public void shouldKnowNotBeASpyIfNoSpiedInstance() throws Exception {
        //when
        mockSettingsImpl.spiedInstance(null);
        
        //then
        assertFalse(mockSettingsImpl.isSpy());
    }
}