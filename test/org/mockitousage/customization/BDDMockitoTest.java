/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.customization;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitousage.MethodsImpl;
import org.mockitoutil.TestBase;

import java.util.Set;

import static org.mockito.BDDMockito.*;

public class BDDMockitoTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void should_stub() throws Exception {
        given(mock.simpleMethod("foo")).willReturn("bar");

        assertEquals("bar", mock.simpleMethod("foo"));
        assertEquals(null, mock.simpleMethod("whatever"));
    }

    @Test
    public void should_stub_with_throwable() throws Exception {
        given(mock.simpleMethod("foo")).willThrow(new RuntimeException());

        try {
            assertEquals("foo", mock.simpleMethod("foo"));
            fail();
        } catch(RuntimeException e) {}
    }

    @Test(expected = NullPointerException.class)
    public void should_stub_with_throwable_consecutively() throws Exception {
        given(mock.simpleMethod("foo")).willThrow(new RuntimeException(), new NullPointerException());

        try {
            assertEquals("foo", mock.simpleMethod("foo"));
            fail();
        } catch(RuntimeException e) {}
        assertEquals("foo", mock.simpleMethod("foo"));
    }

    @Test
    public void should_stub_mutiple_times_with_throwable_consecutively() throws Exception {
        given(mock.simpleMethod("foo")).willThrow(new RuntimeException(), new NullPointerException());

        try {
            assertEquals("foo", mock.simpleMethod("foo"));
            fail();
        } catch(RuntimeException e) {}
        for (int i =0; i< 5 ; ++i){
            try {
                assertEquals("foo", mock.simpleMethod("foo"));
                fail();
            } catch(NullPointerException e) {}
        }
    }

    @Test
    public void should_stub_with_throwable_class() throws Exception {
        given(mock.simpleMethod("foo")).willThrow(RuntimeException.class);

        try {
            assertEquals("foo", mock.simpleMethod("foo"));
            fail();
        } catch(RuntimeException e) {}
    }

    @Test(expected = NullPointerException.class)
    public void should_stub_with_throwable_class_consecutively() throws Exception {
        given(mock.simpleMethod("foo")).willThrow(RuntimeException.class, NullPointerException.class);

        try {
            assertEquals("foo", mock.simpleMethod("foo"));
            fail();
        } catch(RuntimeException e) {}
        assertEquals("foo", mock.simpleMethod("foo"));
    }

    @Test
    public void should_stub_multiple_times_with_throwable_class_consecutively() throws Exception {
        given(mock.simpleMethod("foo")).willThrow(RuntimeException.class, NullPointerException.class);

        try {
            assertEquals("foo", mock.simpleMethod("foo"));
            fail();
        } catch(RuntimeException e) {}
        for (int i = 0 ; i < 5 ; ++i){
            try {
                assertEquals("foo", mock.simpleMethod("foo"));
                fail();
            } catch(NullPointerException e) {}
        }

    }

    @Test
    public void should_stub_with_answer() throws Exception {
        given(mock.simpleMethod(anyString())).willAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArguments()[0];
            }});

        assertEquals("foo", mock.simpleMethod("foo"));
    }

    @Test
    public void should_stub_with_will_answer_alias() throws Exception {
        given(mock.simpleMethod(anyString())).will(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArguments()[0];
            }});

        assertEquals("foo", mock.simpleMethod("foo"));
    }

    @Test
    public void should_stub_consecutively() throws Exception {
       given(mock.simpleMethod(anyString()))
           .willReturn("foo")
           .willReturn("bar");

       assertEquals("foo", mock.simpleMethod("whatever"));
       assertEquals("bar", mock.simpleMethod("whatever"));
    }

    @Test
    public void should_stub_consecutively_by_single_invocation() throws Exception {
        given(mock.simpleMethod("foo")).willReturn("bar0","bar1");

        assertEquals("bar0", mock.simpleMethod("foo"));
        assertEquals(null, mock.simpleMethod("whatever"));
        assertEquals("bar1", mock.simpleMethod("foo"));
        for (int i = 0; i< 5 ; ++i){
            assertEquals("bar1", mock.simpleMethod("foo"));
            assertEquals(null, mock.simpleMethod("other"));
        }
    }

    @Test
    public void should_stub_consecutively_with_call_real_method() throws Exception {
        MethodsImpl mock = mock(MethodsImpl.class);
        willReturn("foo").willCallRealMethod()
                .given(mock).simpleMethod();

       assertEquals("foo", mock.simpleMethod());
       assertEquals(null, mock.simpleMethod());
    }

    @Test
    public void should_stub_void() throws Exception {
        willThrow(new RuntimeException()).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch(RuntimeException e) {}
    }

    @Test
    public void should_stub_void_with_exception_class() throws Exception {
        willThrow(RuntimeException.class).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch(RuntimeException e) {}
    }

    @Test
    public void should_stub_void_consecutively() throws Exception {
        willDoNothing()
        .willThrow(new RuntimeException())
        .given(mock).voidMethod();

        mock.voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch(RuntimeException e) {}
    }

    @Test
    public void should_stub_void_consecutively_with_exception_class() throws Exception {
        willDoNothing()
        .willThrow(IllegalArgumentException.class)
        .given(mock).voidMethod();

        mock.voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch(IllegalArgumentException e) {}
    }

    @Test
    public void should_stub_using_do_return_style() throws Exception {
        willReturn("foo").given(mock).simpleMethod("bar");

        assertEquals(null, mock.simpleMethod("boooo"));
        assertEquals("foo", mock.simpleMethod("bar"));
    }

    @Test
    public void should_stub_using_do_answer_style() throws Exception {
        willAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArguments()[0];
            }})
        .given(mock).simpleMethod(anyString());

        assertEquals("foo", mock.simpleMethod("foo"));
    }

    class Dog {
        public String bark() {
            return "woof";
        }
    }

    @Test
    public void should_stub_by_delegating_to_real_method() throws Exception {
        //given
        Dog dog = mock(Dog.class);
        //when
        willCallRealMethod().given(dog).bark();
        //then
        assertEquals("woof", dog.bark());
    }

    @Test
    public void should_stub_by_delegating_to_real_method_using_typical_stubbing_syntax() throws Exception {
        //given
        Dog dog = mock(Dog.class);
        //when
        given(dog.bark()).willCallRealMethod();
        //then
        assertEquals("woof", dog.bark());
    }

    @Test
    public void should_all_stubbed_mock_reference_access() throws Exception {
        Set expectedMock = mock(Set.class);

        Set returnedMock = given(expectedMock.isEmpty()).willReturn(false).getMock();

        assertEquals(expectedMock, returnedMock);
    }

    @Test(expected = NotAMockException.class)
    public void shouldValidateMockWhenVerifying() {

        then("notMock").should();
    }

    @Test(expected = NotAMockException.class)
    public void shouldValidateMockWhenVerifyingWithExpectedNumberOfInvocations() {

        then("notMock").should(times(19));
    }

    @Test(expected = NotAMockException.class)
    public void shouldValidateMockWhenVerifyingNoMoreInteractions() {

        then("notMock").should();
    }

    @Test(expected = WantedButNotInvoked.class)
    public void shouldFailForExpectedBehaviorThatDidNotHappen() {

        then(mock).should().booleanObjectReturningMethod();
    }

    @Test
    public void shouldPassForExpectedBehaviorThatHappened() {

        mock.booleanObjectReturningMethod();

        then(mock).should().booleanObjectReturningMethod();
    }

    @Test
    public void shouldPassFluentBddScenario() {

        Bike bike = new Bike();
        Person person = mock(Person.class);

        person.ride(bike);
        person.ride(bike);

        then(person).should(times(2)).ride(bike);
    }

    static class Person {

        void ride(Bike bike) {}
    }

    static class Bike {

    }
}
