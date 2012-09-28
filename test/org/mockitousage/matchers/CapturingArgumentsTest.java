/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class CapturingArgumentsTest extends TestBase {

    @Mock IMethods mock;
    
    class Person {

        private final Integer age;

        public Person(Integer age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }
    
    class Emailer {
     
        private EmailService service;
        
        public Emailer(EmailService service) {
            this.service = service;
        }

        public void email(Integer ... personId) {
            for (Integer i : personId) {
                Person person = new Person(i);
                service.sendEmailTo(person);
            }
        }
    }
    
    interface EmailService {
        boolean sendEmailTo(Person person);
    }

    EmailService emailService = mock(EmailService.class);
    Emailer emailer = new Emailer(emailService);

    @SuppressWarnings("deprecation")
    @Test
    public void should_allow_assertions_on_captured_argument() {
        //when
        emailer.email(12);
        
        //then
        ArgumentCaptor<Person> argument = new ArgumentCaptor<Person>();
        verify(emailService).sendEmailTo(argument.capture());
        
        assertEquals(12, argument.getValue().getAge());
    }
    
    @Test
    public void should_allow_assertions_on_all_captured_arguments() {
        //when
        emailer.email(11, 12);
        
        //then
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(emailService, atLeastOnce()).sendEmailTo(argument.capture());
        List<Person> allValues = argument.getAllValues();
        
        assertEquals(11, allValues.get(0).getAge());
        assertEquals(12, allValues.get(1).getAge());
    }
    
    @Test
    public void should_allow_assertions_on_last_argument() {
        //when
        emailer.email(11, 12, 13);
        
        //then
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(emailService, atLeastOnce()).sendEmailTo(argument.capture());
        
        assertEquals(13, argument.getValue().getAge());
    }
    
    @Test
    public void should_print_captor_matcher() {
        //given
        ArgumentCaptor<Person> person = ArgumentCaptor.forClass(Person.class);
        
        try {
            //when
            verify(emailService).sendEmailTo(person.capture());
            fail();
        } catch(WantedButNotInvoked e) {
            //then
            assertContains("<Capturing argument>", e.getMessage());
        }
    }
    
    @Test
    public void should_allow_assertions_on_captured_null() {
        //when
        emailService.sendEmailTo(null);
        
        //then
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(emailService).sendEmailTo(argument.capture());
        assertEquals(null, argument.getValue());
    }
    
    @Test
    public void should_allow_capturing_for_stubbing() {
        //given
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        when(emailService.sendEmailTo(argument.capture())).thenReturn(false);
        
        //when
        emailService.sendEmailTo(new Person(10));
        
        //then
        assertEquals(10, argument.getValue().getAge());
    }
    
    @Test
    public void should_capture_when_stubbing_only_when_entire_invocation_matches() {
        //given
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(mock.simpleMethod(argument.capture(), eq(2))).thenReturn("blah");
        
        //when
        mock.simpleMethod("foo", 200);
        mock.simpleMethod("bar", 2);
        
        //then
        Assertions.assertThat(argument.getAllValues()).containsOnly("bar");
    }
    
    @Test
    public void should_say_something_smart_when_misused() {
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        try {
            argument.getValue();
            fail();
        } catch (MockitoException e) {}
    }
    
    @Test
    public void should_capture_when_full_arg_list_matches() throws Exception {
        //given
        mock.simpleMethod("foo", 1);
        mock.simpleMethod("bar", 2);
        
        //when
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mock).simpleMethod(captor.capture(), eq(1));
        
        //then
        assertEquals(1, captor.getAllValues().size());
        assertEquals("foo", captor.getValue());
    }
    
    @Test
    public void should_capture_int_by_creating_captor_with_primitive_wrapper() {
        //given
        IMethods mock = mock(IMethods.class);
        ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);

        //when
        mock.intArgumentMethod(10);
        
        //then
        verify(mock).intArgumentMethod(argument.capture());
        assertEquals(10, (int) argument.getValue());
    }

    @Test
    public void should_capture_int_by_creating_captor_with_primitive() throws Exception {
        //given
        IMethods mock = mock(IMethods.class);
        ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(int.class);
        
        //when
        mock.intArgumentMethod(10);
        
        //then
        verify(mock).intArgumentMethod(argument.capture());
        assertEquals(10, (int) argument.getValue());
    }
}