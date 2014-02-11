/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stubbing;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitoutil.TestBase;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@SuppressWarnings({"serial", "unchecked", "all", "deprecation"})
public class StubbingWithThrowablesTest extends TestBase {

    private LinkedList mock;
    private Map mockTwo;

    @Before 
    public void setup() {
        mock = mock(LinkedList.class);
        mockTwo = mock(HashMap.class);
    }
    
    @Test
    public void should_stub_with_throwable() throws Exception {
        IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");
        when(mock.add("throw")).thenThrow(expected);
        
        try {
            mock.add("throw");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(expected, e);
        }
    }
    
    @Test
    public void should_set_throwable_to_void_method() throws Exception {
        IllegalArgumentException expected = new IllegalArgumentException("thrown by mock");
        
        stubVoid(mock).toThrow(expected).on().clear();
        try {
            mock.clear();
            fail();
        } catch (Exception e) {
            assertEquals(expected, e);
        }
    } 
    
    @Test
    public void should_last_stubbing_void_be_important() throws Exception {
        stubVoid(mock).toThrow(new ExceptionOne()).on().clear();
        stubVoid(mock).toThrow(new ExceptionTwo()).on().clear();
        
        try {
            mock.clear();
            fail();
        } catch (ExceptionTwo e) {}
    }
    
    @Test
    public void should_fail_stubbing_throwable_on_the_same_invocation_due_to_acceptable_limitation() throws Exception {
        when(mock.get(1)).thenThrow(new ExceptionOne());
        
        try {
            when(mock.get(1)).thenThrow(new ExceptionTwo());
            fail();
        } catch (ExceptionOne e) {}
    }   
    
    @Test
    public void should_allow_setting_checked_exception() throws Exception {
        Reader reader = mock(Reader.class);
        IOException ioException = new IOException();
        
        when(reader.read()).thenThrow(ioException);
        
        try {
            reader.read();
            fail();
        } catch (Exception e) {
            assertEquals(ioException, e);
        }
    }
    
    @Test
    public void should_allow_setting_error() throws Exception {
        Error error = new Error();
        
        when(mock.add("quake")).thenThrow(error);
        
        try {
            mock.add("quake");
            fail();
        } catch (Error e) {
            assertEquals(error, e);
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void should_instantiate_exception_class_on_interaction() {
        when(mock.add(null)).thenThrow(IllegalArgumentException.class);

        mock.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_instantiate_exception_class_with_ongoing_stubbing_on_interaction() {
        Mockito.doThrow(IllegalArgumentException.class).when(mock).add(null);

        mock.add(null);
    }

    @Test(expected=ExceptionTwo.class)
    public void should_throw_ExceptionTwo_after_ExceptionOne(){
        List list = Mockito.mock(List.class);
        doThrow(ExceptionOne.class,ExceptionTwo.class).when(list).add(null);
        try{
            list.add(null);
            fail();
        }catch(ExceptionOne e){}

        list.add(null);
    }

    @Test
    public void should_throw_ExceptionTwo_multiple_times_after_ExceptionOne(){
        List list = Mockito.mock(List.class);
        doThrow(ExceptionOne.class,ExceptionTwo.class).when(list).add(null);
        try{
            list.add(null);
            fail();
        }catch(ExceptionOne e){}

        for (int i=0 ; i<5 ; ++i){
            try{
                list.add(null);
                fail();
            } catch(ExceptionTwo e){}
        }
    }
    
    @Test(expected=MockitoException.class)
    public void should_not_allow_setting_invalid_checked_exception() throws Exception {
        when(mock.add("monkey island")).thenThrow(new Exception());
    }
    
    @Test(expected=MockitoException.class)
    public void should_not_allow_setting_null_throwable() throws Exception {
        when(mock.add("monkey island")).thenThrow((Throwable) null);
    }

    @Test(expected=MockitoException.class)
    public void should_not_allow_setting_null_throwable_array() throws Exception {
        when(mock.add("monkey island")).thenThrow((Throwable[]) null);
    }
    
    @Test
    public void should_mix_throwables_and_returns_on_different_mocks() throws Exception {
        when(mock.add("ExceptionOne")).thenThrow(new ExceptionOne());
        when(mock.getLast()).thenReturn("last");
        stubVoid(mock).toThrow(new ExceptionTwo()).on().clear();
        
        stubVoid(mockTwo).toThrow(new ExceptionThree()).on().clear();
        when(mockTwo.containsValue("ExceptionFour")).thenThrow(new ExceptionFour());
        when(mockTwo.get("Are you there?")).thenReturn("Yes!");

        assertNull(mockTwo.get("foo"));
        assertTrue(mockTwo.keySet().isEmpty());
        assertEquals("Yes!", mockTwo.get("Are you there?"));
        try {
            mockTwo.clear();
            fail();
        } catch (ExceptionThree e) {}
        try {
            mockTwo.containsValue("ExceptionFour");
            fail();
        } catch (ExceptionFour e) {}
        
        assertNull(mock.getFirst());
        assertEquals("last", mock.getLast());
        try {
            mock.add("ExceptionOne");
            fail();
        } catch (ExceptionOne e) {}
        try {
            mock.clear();
            fail();
        } catch (ExceptionTwo e) {}
    }
    
    @Test
    public void should_stubbing_with_throwable_be_verifiable() {
        when(mock.size()).thenThrow(new RuntimeException());
        stubVoid(mock).toThrow(new RuntimeException()).on().clone();
        
        try {
            mock.size();
            fail();
        } catch (RuntimeException e) {}
        
        try {
            mock.clone();
            fail();
        } catch (RuntimeException e) {}
        
        verify(mock).size();
        verify(mock).clone();
        verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void should_stubbing_with_throwable_fail_verification() {
        when(mock.size()).thenThrow(new RuntimeException());
        stubVoid(mock).toThrow(new RuntimeException()).on().clone();
        
        verifyZeroInteractions(mock);
        
        mock.add("test");
        
        try {
            verify(mock).size();
            fail();
        } catch (WantedButNotInvoked e) {}
        
        try {
            verify(mock).clone();
            fail();
        } catch (WantedButNotInvoked e) {}
        
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {}
    }
    
    private class ExceptionOne extends RuntimeException {}
    private class ExceptionTwo extends RuntimeException {}
    private class ExceptionThree extends RuntimeException {}
    private class ExceptionFour extends RuntimeException {}

    public class NaughtyException extends RuntimeException {
        public NaughtyException() {
            throw new RuntimeException("boo!");
        }
    }

    @Test(expected = NaughtyException.class)
    public void should_show_decent_message_when_excepion_is_naughty() throws Exception {
        when(mock.add("")).thenThrow(NaughtyException.class);
        mock.add("");
    }
}