/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.matchers;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.lt;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class CapturingArgumentsTest extends TestBase {

    private static final String TEST_VALUE_1 = "bar";
    private static final String TEST_VALUE_2 = "foo";

	class Person {

        private final Integer age;

        public Person(Integer age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }

    class BulkEmailService {

        private EmailService service;

        public BulkEmailService(EmailService service) {
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
    BulkEmailService bulkEmailService = new BulkEmailService(emailService);
    IMethods mock = mock(IMethods.class);

    @SuppressWarnings("deprecation")
    @Test
    public void should_allow_assertions_on_captured_argument() {
        //given
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);

        //when
        bulkEmailService.email(12);

        //then
        verify(emailService).sendEmailTo(argument.capture());
        assertEquals(12, argument.getValue().getAge());
    }
    
    @Test
    public void should_allow_assertions_on_all_captured_arguments() {
        //given
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);

        //when
        bulkEmailService.email(11, 12);

        //then
        verify(emailService, times(2)).sendEmailTo(argument.capture());
        assertEquals(11, argument.getAllValues().get(0).getAge());
        assertEquals(12, argument.getAllValues().get(1).getAge());
    }
    
    @Test
    public void should_allow_assertions_on_last_argument() {
        //given
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);

        //when
        bulkEmailService.email(11, 12, 13);

        //then
        verify(emailService, times(3)).sendEmailTo(argument.capture());
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
        //given
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);

        //when
        emailService.sendEmailTo(null);

        //then
        verify(emailService).sendEmailTo(argument.capture());
        assertEquals(null, argument.getValue());
    }

    @Test
    public void should_allow_construction_of_captor_for_parameterized_type_in_a_convenient_way()  {
        //the test passes if this expression compiles
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Person>> argument = ArgumentCaptor.forClass(List.class);
        assertNotNull(argument);
    }

    @Test
    public void should_allow_construction_of_captor_for_a_more_specific_type()  {
        //the test passes if this expression compiles
        ArgumentCaptor<List<?>> argument = ArgumentCaptor.forClass(ArrayList.class);
        assertNotNull(argument);
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
        mock.simpleMethod(TEST_VALUE_2, 200);
        mock.simpleMethod(TEST_VALUE_1, 2);
        
        //then
        Assertions.assertThat(argument.getAllValues()).containsOnly(TEST_VALUE_1);
    }
    
    @Test
    public void should_say_something_smart_when_misused() {
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        try {
            argument.getValue();
            fail();
        } catch (MockitoException expected) { }
    }
    
    @Test
    public void should_capture_when_full_arg_list_matches() throws Exception {
        //given
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        //when
        mock.simpleMethod(TEST_VALUE_2, 1);
        mock.simpleMethod(TEST_VALUE_1, 2);

        //then
        verify(mock).simpleMethod(captor.capture(), eq(1));
        assertEquals(1, captor.getAllValues().size());
        assertEquals(TEST_VALUE_2, captor.getValue());
    }
    
    @Test
    public void should_capture_int_by_creating_captor_with_primitive_wrapper() {
        //given
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
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.mixedVarargs(42, "a", "b", "c");
        mock.mixedVarargs(42, "again ?!");

        // then
        verify(mock, times(2)).mixedVarargs(any(), argumentCaptor.capture());

        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly("a", "b", "c", "again ?!");
    }

    @Test
    public void should_capture_one_arg_even_when_using_vararg_captor_on_nonvararg_method() throws Exception {
        // given
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.simpleMethod("a", 2);

        // then
        verify(mock).simpleMethod(argumentCaptor.capture(), eq(2));
        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly("a");
    }

    @Test
    public void captures_correctly_when_captor_used_multiple_times() throws Exception {
        // given
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.mixedVarargs(42, "a", "b", "c");

        // then
        // this is only for backwards compatibility. It does not make sense in real to do so.
        verify(mock).mixedVarargs(any(), argumentCaptor.capture(), argumentCaptor.capture(), argumentCaptor.capture());
        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly("a", "b", "c");
    }

    @Test
    public void captures_correctly_when_captor_used_on_pure_vararg_method() throws Exception {
        // given
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.varargs(42, "capturedValue");

        // then
        verify(mock).varargs(eq(42), argumentCaptor.capture());
        Assertions.assertThat(argumentCaptor.getValue()).contains("capturedValue");
    }

    @Test
    public void should_capture_only_list_instances() throws Exception {
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        mock.forCollection(new ArrayList<String>());
        mock.forCollection(new HashSet<String>());
        mock.forCollection(new LinkedList<String>());

        verify(mock, times(2)).forCollection(captor.captureIf(ArgumentMatchers.isA(List.class)));
        assertThat(2, is(equalTo(captor.getAllValues().size())));
    }

    @Test
    public void should_capture_when_value_is_equal_to_a_certain_value() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        mock.simpleMethod(TEST_VALUE_2, 1);
        mock.simpleMethod(TEST_VALUE_1, 2);

        verify(mock).simpleMethod(captor.captureIf(ArgumentMatchers.eq(TEST_VALUE_2)), anyInt());
        assertThat(1, is(equalTo(captor.getAllValues().size())));
        assertThat(TEST_VALUE_2, is(equalTo(captor.getValue())));
    }

    @Test
    public void should_capture_when_value_is_less_then() {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        mock.simpleMethod(TEST_VALUE_2, 1);
        mock.simpleMethod(TEST_VALUE_1, 2);
        mock.simpleMethod(TEST_VALUE_2, 3);
        mock.simpleMethod(TEST_VALUE_1, 4);
        
        mock.simpleMethod(TEST_VALUE_2, 5);

        verify(mock, times(2)).simpleMethod(anyString(), captor.captureIf(lt(3)));
        assertThat(2, is(equalTo(captor.getAllValues().size())));
        assertThat(Arrays.asList(new Integer[] {1, 2}), is(equalTo(captor.getAllValues())));
    }

    @Test
    public void should_capture_when_only_by_argument_type() {
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        mock.forCollection(new ArrayList<String>());
        mock.forCollection(new HashSet<String>());
        mock.forCollection(new LinkedList<String>());
        
        verify(mock, times(2)).forCollection(captor.capture());
        assertThat(2, is(equalTo(captor.getAllValues().size())));
    }

    @Test
    public void should_capture_all_arguments_ignoring_type() {
        ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(null);

        mock.forCollection(new ArrayList<String>());
        mock.forCollection(new HashSet<String>());
        mock.forCollection(new LinkedList<String>());
        
        verify(mock, times(3)).forCollection(captor.capture());
        assertThat(3, is(equalTo(captor.getAllValues().size())));
    }

    @Test
    public void captures_correctly_when_the_same_type_used_multiple_times() throws Exception {
        // given
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.mixedVarargs(42, "a", "b", "c");

        // then
        verify(mock).mixedVarargs(any(), argumentCaptor.captureIf(isA(String.class)), argumentCaptor.captureIf(isA(String.class)), argumentCaptor.captureIf(isA(String.class)));
        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly("a", "b", "c");
    }

    @Test
    public void captures_correctly_when_capture_and_captureIf_is_used() throws Exception {
        // given
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // when
        mock.mixedVarargs(42, "a", "b", "c");

        // then
        verify(mock).mixedVarargs(any(), argumentCaptor.captureIf(isA(String.class)), argumentCaptor.capture(), argumentCaptor.captureIf(isA(String.class)));
        Assertions.assertThat(argumentCaptor.getAllValues()).containsExactly("a", "b", "c");
    }
}
