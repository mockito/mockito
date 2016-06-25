/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.customization;

import org.assertj.core.api.Assertions;
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

import java.util.Set;

import static junit.framework.TestCase.fail;
import static org.mockito.BDDMockito.*;

public class BDDMockitoTest extends TestBase {

    @Mock
    IMethods mock;

    @Test
    public void should_stub() throws Exception {
        given(mock.simpleMethod("foo")).willReturn("bar");

        Assertions.assertThat(mock.simpleMethod("foo")).isEqualTo("bar");
        Assertions.assertThat(mock.simpleMethod("whatever")).isEqualTo(null);
    }

    @Test
    public void should_stub_with_throwable() throws Exception {
        given(mock.simpleMethod("foo")).willThrow(new SomethingWasWrong());

        try {
            Assertions.assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_with_throwable_class() throws Exception {
        given(mock.simpleMethod("foo")).willThrow(SomethingWasWrong.class);

        try {
            Assertions.assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_stub_with_throwable_classes() throws Exception {
        // unavoidable 'unchecked generic array creation' warning (from JDK7 onward)
        given(mock.simpleMethod("foo")).willThrow(SomethingWasWrong.class, AnotherThingWasWrong.class);

        try {
            Assertions.assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_with_answer() throws Exception {
        given(mock.simpleMethod(anyString())).willAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });

        Assertions.assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
    }

    @Test
    public void should_stub_with_will_answer_alias() throws Exception {
        given(mock.simpleMethod(anyString())).will(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });

        Assertions.assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
    }

    @Test
    public void should_stub_consecutively() throws Exception {
        given(mock.simpleMethod(anyString()))
                .willReturn("foo")
                .willReturn("bar");

        Assertions.assertThat(mock.simpleMethod("whatever")).isEqualTo("foo");
        Assertions.assertThat(mock.simpleMethod("whatever")).isEqualTo("bar");
    }

    @Test
    public void should_return_consecutively() throws Exception {
        given(mock.objectReturningMethodNoArgs())
                .willReturn("foo", "bar", 12L, new byte[0]);

        Assertions.assertThat(mock.objectReturningMethodNoArgs()).isEqualTo("foo");
        Assertions.assertThat(mock.objectReturningMethodNoArgs()).isEqualTo("bar");
        Assertions.assertThat(mock.objectReturningMethodNoArgs()).isEqualTo(12L);
        Assertions.assertThat(mock.objectReturningMethodNoArgs()).isEqualTo(new byte[0]);
    }

    @Test
    public void should_stub_consecutively_with_call_real_method() throws Exception {
        MethodsImpl mock = mock(MethodsImpl.class);
        willReturn("foo").willCallRealMethod()
                .given(mock).simpleMethod();

        Assertions.assertThat(mock.simpleMethod()).isEqualTo("foo");
        Assertions.assertThat(mock.simpleMethod()).isEqualTo(null);
    }

    @Test
    public void should_stub_void() throws Exception {
        willThrow(new SomethingWasWrong()).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_void_with_exception_class() throws Exception {
        willThrow(SomethingWasWrong.class).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_stub_void_with_exception_classes() throws Exception {
        willThrow(SomethingWasWrong.class, AnotherThingWasWrong.class).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch (SomethingWasWrong expected) {
        }
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
        } catch (SomethingWasWrong expected) {
        }
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
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_using_do_return_style() throws Exception {
        willReturn("foo").given(mock).simpleMethod("bar");

        Assertions.assertThat(mock.simpleMethod("boooo")).isEqualTo(null);
        Assertions.assertThat(mock.simpleMethod("bar")).isEqualTo("foo");
    }

    @Test
    public void should_stub_using_do_answer_style() throws Exception {
        willAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        })
                .given(mock).simpleMethod(anyString());

        Assertions.assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
    }

    @Test
    public void should_stub_by_delegating_to_real_method() throws Exception {
        //given
        Dog dog = mock(Dog.class);
        //when
        willCallRealMethod().given(dog).bark();
        //then
        Assertions.assertThat(dog.bark()).isEqualTo("woof");
    }

    @Test
    public void should_stub_by_delegating_to_real_method_using_typical_stubbing_syntax() throws Exception {
        //given
        Dog dog = mock(Dog.class);
        //when
        given(dog.bark()).willCallRealMethod();
        //then
        Assertions.assertThat(dog.bark()).isEqualTo("woof");
    }

    @Test
    public void should_all_stubbed_mock_reference_access() throws Exception {
        Set<?> expectedMock = mock(Set.class);

        Set<?> returnedMock = given(expectedMock.isEmpty()).willReturn(false).getMock();

        Assertions.assertThat(returnedMock).isEqualTo(expectedMock);
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
        then(mock).shouldHaveNoMoreInteractions();
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
        } catch (NoInteractionsWanted expected) {
        }
    }

    @Test
    public void should_fail_when_mock_had_more_interactions_than_expected() {
        mock.booleanObjectReturningMethod();
        mock.byteObjectReturningMethod();

        then(mock).should().booleanObjectReturningMethod();
        try {
            then(mock).shouldHaveNoMoreInteractions();
            fail("should have reported that no more interactions were wanted");
        } catch (NoInteractionsWanted expected) {
        }
    }

    @Test
    public void should_pass_for_interactions_that_happened_in_correct_order() {
        mock.booleanObjectReturningMethod();
        mock.arrayReturningMethod();

        InOrder inOrder = inOrder(mock);
        then(mock).should(inOrder).booleanObjectReturningMethod();
        then(mock).should(inOrder).arrayReturningMethod();
    }

    @Test
    public void should_fail_for_interactions_that_were_in_wrong_order() {
        InOrder inOrder = inOrder(mock);

        mock.arrayReturningMethod();
        mock.booleanObjectReturningMethod();

        then(mock).should(inOrder).booleanObjectReturningMethod();
        try {
            then(mock).should(inOrder).arrayReturningMethod();
            fail("should have raise in order verification failure on second verify call");
        } catch (VerificationInOrderFailure expected) {
        }
    }

    @Test(expected = WantedButNotInvoked.class)
    public void should_fail_when_checking_order_of_interactions_that_did_not_happen() {
        then(mock).should(inOrder(mock)).booleanObjectReturningMethod();
    }

    @Test
    public void should_pass_fluent_bdd_scenario() {
        Bike bike = new Bike();
        Person person = mock(Person.class);
        Police police = mock(Police.class);

        person.ride(bike);
        person.ride(bike);

        then(person).should(times(2)).ride(bike);
        then(police).shouldHaveZeroInteractions();
    }

    @Test
    public void should_pass_fluent_bdd_scenario_with_ordered_verification() {
        Bike bike = new Bike();
        Car car = new Car();
        Person person = mock(Person.class);

        person.drive(car);
        person.ride(bike);
        person.ride(bike);

        InOrder inOrder = inOrder(person);
        then(person).should(inOrder).drive(car);
        then(person).should(inOrder, times(2)).ride(bike);
    }

    @Test
    public void should_pass_fluent_bdd_scenario_with_ordered_verification_for_two_mocks() {
        Car car = new Car();
        Person person = mock(Person.class);
        Police police = mock(Police.class);

        person.drive(car);
        person.drive(car);
        police.chase(car);

        InOrder inOrder = inOrder(person, police);
        then(person).should(inOrder, times(2)).drive(car);
        then(police).should(inOrder).chase(car);
    }

    static class Person {

        void ride(Bike bike) {
        }

        void drive(Car car) {
        }
    }

    static class Bike {

    }

    static class Car {

    }

    static class Police {

        void chase(Car car) {
        }
    }

    class Dog {

        public String bark() {
            return "woof";
        }
    }

    private class SomethingWasWrong extends RuntimeException {

    }

    private class AnotherThingWasWrong extends RuntimeException {

    }
}
