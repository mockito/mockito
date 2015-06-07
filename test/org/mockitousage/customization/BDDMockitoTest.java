/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.customization;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Set;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitousage.MethodsImpl;
import org.mockitoutil.TestBase;

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
        given(mock.simpleMethod("foo")).willThrow(new SomethingWasWrong());

        try {
            assertEquals("foo", mock.simpleMethod("foo"));
            fail();
        } catch(final SomethingWasWrong expected) {}
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_stub_with_throwable_class() throws Exception {
        given(mock.simpleMethod("foo")).willThrow(SomethingWasWrong.class);

        try {
            assertEquals("foo", mock.simpleMethod("foo"));
            fail();
        } catch(final SomethingWasWrong expected) {}
    }

    @Test
    public void should_stub_with_answer() throws Exception {
        given(mock.simpleMethod(anyString())).willAnswer(new Answer<String>() {
            public String answer(final InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArguments()[0];
            }});

        assertEquals("foo", mock.simpleMethod("foo"));
    }

    @Test
    public void should_stub_with_will_answer_alias() throws Exception {
        given(mock.simpleMethod(anyString())).will(new Answer<String>() {
            public String answer(final InvocationOnMock invocation) throws Throwable {
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
    public void should_stub_consecutively_with_call_real_method() throws Exception {
        final MethodsImpl mock = mock(MethodsImpl.class);
        willReturn("foo").willCallRealMethod()
                .given(mock).simpleMethod();

       assertEquals("foo", mock.simpleMethod());
       assertEquals(null, mock.simpleMethod());
    }

    @Test
    public void should_stub_void() throws Exception {
        willThrow(new SomethingWasWrong()).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch(final SomethingWasWrong expected) {}
    }

    @Test
    public void should_stub_void_with_exception_class() throws Exception {
        willThrow(SomethingWasWrong.class).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch(final SomethingWasWrong expected) {}
    }

    @Test
    public void should_stub_void_consecutively() throws Exception {
        willDoNothing()
                .willThrow(new SomethingWasWrong())
                .given(mock).voidMethod();

        mock.voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch(final SomethingWasWrong expected) {}
    }

    @Test
    public void should_stub_void_consecutively_with_exception_class() throws Exception {
        willDoNothing()
                .willThrow(SomethingWasWrong.class)
                .given(mock).voidMethod();

        mock.voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch(final SomethingWasWrong expected) {}
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
            public String answer(final InvocationOnMock invocation) throws Throwable {
                return (String) invocation.getArguments()[0];
            }})
                .given(mock).simpleMethod(anyString());

        assertEquals("foo", mock.simpleMethod("foo"));
    }

    @Test
    public void should_stub_by_delegating_to_real_method() throws Exception {
        //given
        final Dog dog = mock(Dog.class);
        //when
        willCallRealMethod().given(dog).bark();
        //then
        assertEquals("woof", dog.bark());
    }

    @Test
    public void should_stub_by_delegating_to_real_method_using_typical_stubbing_syntax() throws Exception {
        //given
        final Dog dog = mock(Dog.class);
        //when
        given(dog.bark()).willCallRealMethod();
        //then
        assertEquals("woof", dog.bark());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void should_all_stubbed_mock_reference_access() throws Exception {
        final Set expectedMock = mock(Set.class);

        final Set returnedMock = given(expectedMock.isEmpty()).willReturn(false).getMock();

        assertEquals(expectedMock, returnedMock);
    }

    @Test(expected = NotAMockException.class)
    public void should_validate_mock_when_verifying() {
        then("notMock").should();
    }

    @Test(expected = NotAMockException.class)
    public void should_validate_mock_when_verifying_with_expected_number_of_invocations() {
        then("notMock").should(times(19));
    }

    @Test(expected = NotAMockException.class)
    public void should_validate_mock_when_verifying_no_more_interactions() {
        then("notMock").should();
    }

    @Test(expected = WantedButNotInvoked.class)
    public void should_fail_for_expected_behavior_that_did_not_happen() {
        then(mock).should().booleanObjectReturningMethod();
    }

    @Test
    public void should_pass_for_expected_behavior_that_happened() {
        mock.booleanObjectReturningMethod();

        then(mock).should().booleanObjectReturningMethod();
    }

    @Test
    public void should_validate_that_mock_did_not_have_any_interactions() {
        then(mock).shouldHaveZeroInteractions();
    }

    @Test
    public void should_fail_when_mock_had_unwanted_interactions() {
        mock.booleanObjectReturningMethod();

        try {
            then(mock).shouldHaveZeroInteractions();
            fail("should have reported this interaction wasn't wanted");
        } catch (final NoInteractionsWanted expected) { }
    }

    @Test
    public void should_pass_for_interactions_that_happened_in_correct_order() {
        mock.booleanObjectReturningMethod();
        mock.arrayReturningMethod();

        final InOrder inOrder = inOrder(mock);
        then(mock).should(inOrder).booleanObjectReturningMethod();
        then(mock).should(inOrder).arrayReturningMethod();
    }

    @Test
    public void should_fail_for_interactions_that_were_in_wrong_order() {
        final InOrder inOrder = inOrder(mock);

        mock.arrayReturningMethod();
        mock.booleanObjectReturningMethod();

        then(mock).should(inOrder).booleanObjectReturningMethod();
        try {
            then(mock).should(inOrder).arrayReturningMethod();
            fail("should have raise in order verification failure on second verify call");
        } catch (final VerificationInOrderFailure expected) { }
    }

    @Test(expected = WantedButNotInvoked.class)
    public void should_fail_when_checking_order_of_interactions_that_did_not_happen() {
        then(mock).should(inOrder(mock)).booleanObjectReturningMethod();
    }

    @Test
    public void should_pass_fluent_bdd_scenario() {
        final Bike bike = new Bike();
        final Person person = mock(Person.class);
        final Police police = mock(Police.class);

        person.ride(bike);
        person.ride(bike);

        then(person).should(times(2)).ride(bike);
        then(police).shouldHaveZeroInteractions();
    }

    @Test
    public void should_pass_fluent_bdd_scenario_with_ordered_verification() {
        final Bike bike = new Bike();
        final Car car = new Car();
        final Person person = mock(Person.class);

        person.drive(car);
        person.ride(bike);
        person.ride(bike);

        final InOrder inOrder = inOrder(person);
        then(person).should(inOrder).drive(car);
        then(person).should(inOrder, times(2)).ride(bike);
    }

    @Test
    public void should_pass_fluent_bdd_scenario_with_ordered_verification_for_two_mocks() {
        final Car car = new Car();
        final Person person = mock(Person.class);
        final Police police = mock(Police.class);

        person.drive(car);
        person.drive(car);
        police.chase(car);

        final InOrder inOrder = inOrder(person, police);
        then(person).should(inOrder, times(2)).drive(car);
        then(police).should(inOrder).chase(car);
    }

    static class Person {

        void ride(final Bike bike) {}
        void drive(final Car car) {}
    }
    static class Bike { }

    static class Car { }
    static class Police {

        void chase(final Car car) {}
    }

    class Dog {
        public String bark() {
            return "woof";
        }
    }

    @SuppressWarnings("serial")
    private class SomethingWasWrong extends RuntimeException { }
}
