/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.customization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

import java.util.Set;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitousage.MethodsImpl;
import org.mockitoutil.TestBase;

public class BDDMockitoTest extends TestBase {

    @Mock IMethods mock;

    @Test
    public void should_stub() {
        given(mock.simpleMethod("foo")).willReturn("bar");

        assertThat(mock.simpleMethod("foo")).isEqualTo("bar");
        assertThat(mock.simpleMethod("whatever")).isNull();
    }

    @Test
    public void should_stub_with_throwable() {
        given(mock.simpleMethod("foo")).willThrow(new SomethingWasWrong());

        try {
            assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_with_throwable_class() {
        given(mock.simpleMethod("foo")).willThrow(SomethingWasWrong.class);

        try {
            assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_stub_with_throwable_classes() {
        // unavoidable 'unchecked generic array creation' warning (from JDK7 onward)
        given(mock.simpleMethod("foo"))
                .willThrow(SomethingWasWrong.class, AnotherThingWasWrong.class);

        try {
            assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_with_answer() {
        given(mock.simpleMethod(anyString()))
                .willAnswer(
                        (Answer<String>)
                                invocation -> {
                                    return invocation.getArgument(0);
                                });

        assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
    }

    @Test
    public void should_stub_with_will_answer_alias() {
        given(mock.simpleMethod(anyString()))
                .will(
                        (Answer<String>)
                                invocation -> {
                                    return invocation.getArgument(0);
                                });

        assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
    }

    @Test
    public void should_stub_consecutively() {
        given(mock.simpleMethod(anyString())).willReturn("foo").willReturn("bar");

        assertThat(mock.simpleMethod("whatever")).isEqualTo("foo");
        assertThat(mock.simpleMethod("whatever")).isEqualTo("bar");
    }

    @Test
    public void should_return_consecutively() {
        given(mock.objectReturningMethodNoArgs()).willReturn("foo", "bar", 12L, new byte[0]);

        assertThat(mock.objectReturningMethodNoArgs()).isEqualTo("foo");
        assertThat(mock.objectReturningMethodNoArgs()).isEqualTo("bar");
        assertThat(mock.objectReturningMethodNoArgs()).isEqualTo(12L);
        assertThat(mock.objectReturningMethodNoArgs()).isEqualTo(new byte[0]);
    }

    @Test
    public void should_stub_consecutively_with_call_real_method() {
        MethodsImpl mock = mock(MethodsImpl.class);
        willReturn("foo").willCallRealMethod().given(mock).simpleMethod();

        assertThat(mock.simpleMethod()).isEqualTo("foo");
        assertThat(mock.simpleMethod()).isNull();
    }

    @Test
    public void should_stub_void() {
        willThrow(new SomethingWasWrong()).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_void_with_exception_class() {
        willThrow(SomethingWasWrong.class).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void should_stub_void_with_exception_classes() {
        willThrow(SomethingWasWrong.class, AnotherThingWasWrong.class).given(mock).voidMethod();

        try {
            mock.voidMethod();
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_void_consecutively() {
        willDoNothing().willThrow(new SomethingWasWrong()).given(mock).voidMethod();

        mock.voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_void_consecutively_with_exception_class() {
        willDoNothing().willThrow(SomethingWasWrong.class).given(mock).voidMethod();

        mock.voidMethod();
        try {
            mock.voidMethod();
            fail();
        } catch (SomethingWasWrong expected) {
        }
    }

    @Test
    public void should_stub_using_do_return_style() {
        willReturn("foo").given(mock).simpleMethod("bar");

        assertThat(mock.simpleMethod("boooo")).isEqualTo(null);
        assertThat(mock.simpleMethod("bar")).isEqualTo("foo");
    }

    @Test
    public void should_stub_using_do_answer_style() {
        willAnswer(
                        (Answer<String>)
                                invocation -> {
                                    return invocation.getArgument(0);
                                })
                .given(mock)
                .simpleMethod(anyString());

        assertThat(mock.simpleMethod("foo")).isEqualTo("foo");
    }

    @Test
    public void should_stub_by_delegating_to_real_method() {
        // given
        Dog dog = mock(Dog.class);
        // when
        willCallRealMethod().given(dog).bark();
        // then
        assertThat(dog.bark()).isEqualTo("woof");
    }

    @Test
    public void should_stub_by_delegating_to_real_method_using_typical_stubbing_syntax() {
        // given
        Dog dog = mock(Dog.class);
        // when
        given(dog.bark()).willCallRealMethod();
        // then
        assertThat(dog.bark()).isEqualTo("woof");
    }

    @Test
    public void should_all_stubbed_mock_reference_access() {
        Set<?> expectedMock = mock(Set.class);

        Set<?> returnedMock = given(expectedMock.isEmpty()).willReturn(false).getMock();

        assertThat(returnedMock).isEqualTo(expectedMock);
    }

    @Test
    public void should_validate_mock_when_verifying() {
        assertThatThrownBy(
                        () -> {
                            then("notMock").should();
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContainingAll(
                        "Argument passed to verify() is of type String and is not a mock!",
                        "Make sure you place the parenthesis correctly!",
                        "See the examples of correct verifications:",
                        "    verify(mock).someMethod();",
                        "    verify(mock, times(10)).someMethod();",
                        "    verify(mock, atLeastOnce()).someMethod();");
    }

    @Test
    public void should_validate_mock_when_verifying_with_expected_number_of_invocations() {
        assertThatThrownBy(
                        () -> {
                            then("notMock").should(times(19));
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContainingAll(
                        "Argument passed to verify() is of type String and is not a mock!",
                        "Make sure you place the parenthesis correctly!",
                        "See the examples of correct verifications:",
                        "    verify(mock).someMethod();",
                        "    verify(mock, times(10)).someMethod();",
                        "    verify(mock, atLeastOnce()).someMethod();");
    }

    @Test
    public void should_validate_mock_when_verifying_no_more_interactions() {
        assertThatThrownBy(
                        () -> {
                            then("notMock").shouldHaveNoMoreInteractions();
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContainingAll(
                        "Argument(s) passed is not a mock!",
                        "Examples of correct verifications:",
                        "    verifyNoMoreInteractions(mockOne, mockTwo);",
                        "    verifyNoInteractions(mockOne, mockTwo);");
    }

    @Test
    public void should_fail_for_expected_behavior_that_did_not_happen() {
        assertThatThrownBy(
                        () -> {
                            then(mock).should().booleanObjectReturningMethod();
                        })
                .isInstanceOf(WantedButNotInvoked.class)
                .hasMessageContainingAll(
                        "Wanted but not invoked:", "mock.booleanObjectReturningMethod();");
    }

    @Test
    public void should_pass_for_expected_behavior_that_happened() {
        mock.booleanObjectReturningMethod();

        then(mock).should().booleanObjectReturningMethod();
        then(mock).shouldHaveNoMoreInteractions();
    }

    @Test
    public void should_validate_that_mock_had_no_interactions() {
        then(mock).shouldHaveNoInteractions();
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

    @Test
    public void should_fail_when_checking_order_of_interactions_that_did_not_happen() {
        assertThatThrownBy(
                        () -> {
                            then(mock).should(inOrder(mock)).booleanObjectReturningMethod();
                        })
                .isInstanceOf(WantedButNotInvoked.class)
                .hasMessageContainingAll(
                        "Wanted but not invoked:",
                        "mock.booleanObjectReturningMethod();",
                        "Actually, there were zero interactions with this mock.");
    }

    @Test
    public void should_pass_fluent_bdd_scenario() {
        Bike bike = new Bike();
        Person person = mock(Person.class);
        Police police = mock(Police.class);

        person.ride(bike);
        person.ride(bike);

        then(person).should(times(2)).ride(bike);
        then(police).shouldHaveNoInteractions();
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

        void ride(Bike bike) {}

        void drive(Car car) {}
    }

    static class Bike {}

    static class Car {}

    static class Police {

        void chase(Car car) {}
    }

    class Dog {

        public String bark() {
            return "woof";
        }
    }

    private class SomethingWasWrong extends RuntimeException {}

    private class AnotherThingWasWrong extends RuntimeException {}
}
