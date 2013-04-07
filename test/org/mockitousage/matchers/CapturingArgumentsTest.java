/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

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

    @Test
    public void should_capture_byte_vararg_by_creating_captor_with_primitive() throws Exception {
        // given
        IMethods mock = mock(IMethods.class);
        ArgumentCaptor<Byte> argumentCaptor = ArgumentCaptor.forClass(byte.class);

        // when
        mock.varargsbyte((byte) 1, (byte) 2);

        // then
        verify(mock).varargsbyte(argumentCaptor.capture());
        assertEquals((byte) 2, (byte) argumentCaptor.getValue());
        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly((byte) 1, (byte) 2);
    }

    @Test
    public void should_capture_byte_vararg_by_creating_captor_with_primitive_wrapper() throws Exception {
        // given
        IMethods mock = mock(IMethods.class);
        ArgumentCaptor<Byte> argumentCaptor = ArgumentCaptor.forClass(Byte.class);

        // when
        mock.varargsbyte((byte) 1, (byte) 2);

        // then
        verify(mock).varargsbyte(argumentCaptor.capture());
        assertEquals((byte) 2, (byte) argumentCaptor.getValue());
        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly((byte) 1, (byte) 2);
    }

    @Test
    public void should_capture_vararg() throws Exception {
        // given
        IMethods mock = mock(IMethods.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.mixedVarargs(42, "a", "b", "c");

        // then
        verify(mock).mixedVarargs(any(), argumentCaptor.capture());
        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly("a", "b", "c");
    }

    @Test
    public void should_capture_all_vararg() throws Exception {
        // given
        IMethods mock = mock(IMethods.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.mixedVarargs(42, "a", "b", "c");
        mock.mixedVarargs(42, "again ?!");

        // then
        verify(mock, times(2)).mixedVarargs(any(), argumentCaptor.capture());

        List<String> allVarargsValues = argumentCaptor.getAllValues();
        Assertions.assertThat(allVarargsValues).containsExactly("a", "b", "c", "again ?!");
    }

    @Test
    public void should_capture_one_arg_even_when_using_vararg_captor_on_nonvararg_method() throws Exception {
        // given
        IMethods mock = mock(IMethods.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.simpleMethod("a", 2);

        // then
        verify(mock).simpleMethod(argumentCaptor.capture(), eq(2));
        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly("a");
    }

    @Test
    public void captures_correclty_when_captor_used_multiple_times() throws Exception {
        // given
        IMethods mock = mock(IMethods.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.mixedVarargs(42, "a", "b", "c");

        // then
        // this is only for backwards compatibility. It does not make sense in real to do so.
        verify(mock).mixedVarargs(any(), argumentCaptor.capture(), argumentCaptor.capture(), argumentCaptor.capture());
        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly("a", "b", "c");
    }
}