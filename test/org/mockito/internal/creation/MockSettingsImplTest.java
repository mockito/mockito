package org.mockito.internal.creation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MockSettingsImplTest extends TestBase {

    @Test
    public void shouldNotAllowSettingNullInterface() {
        try {
            //when
            new MockSettingsImpl().extraInterfaces(List.class, null);
            fail();
            //then
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldNotAllowNonInterfaces() {
        try {
            //when
            new MockSettingsImpl().extraInterfaces(List.class, LinkedList.class);
            fail();
            //then
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldNotAllowUsingTheSameInterfaceAsExtra() {
        try {
            //when
            new MockSettingsImpl().extraInterfaces(List.class, LinkedList.class);
            fail();
            //then
        } catch (MockitoException e) {}
    }
    
    @Test
    public void shouldAllowMultipleInterfaces() {
        //given
        MockSettingsImpl settings = new MockSettingsImpl();
        
        //when
        settings.extraInterfaces(List.class, Set.class);
        
        //then
        assertEquals(List.class, settings.getExtraInterfaces()[0]);
        assertEquals(Set.class, settings.getExtraInterfaces()[1]);
    }
}