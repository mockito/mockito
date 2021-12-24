/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooFewActualInvocations;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ReporterTest extends TestBase {

    @Test
    public void should_let_passing_null_last_actual_stack_trace() {
        assertThatThrownBy(
                        () -> {
                            throw Reporter.tooFewActualInvocations(
                                    new org.mockito.internal.reporting.Discrepancy(1, 2),
                                    new InvocationBuilder().toInvocation(),
                                    null);
                        })
                .isInstanceOf(TooFewActualInvocations.class)
                .hasMessageContainingAll(
                        "iMethods.simpleMethod();", "Wanted 1 time:", "But was 2 times:");
    }

    @Test
    public void should_throw_correct_exception_for_null_invocation_listener() {
        assertThatThrownBy(
                        () -> {
                            throw Reporter.methodDoesNotAcceptParameter(
                                    "invocationListeners", "null vararg array");
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessage(
                        "invocationListeners() does not accept null vararg array See the Javadoc.");
    }

    @Test
    public void
            can_use_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_no_more_interaction_wanted() {
        Invocation invocation_with_bogus_default_answer =
                new InvocationBuilder()
                        .mock(mock(IMethods.class, new Returns(false)))
                        .toInvocation();

        assertThatThrownBy(
                        () -> {
                            throw Reporter.noMoreInteractionsWanted(
                                    invocation_with_bogus_default_answer,
                                    Collections.<VerificationAwareInvocation>emptyList());
                        })
                .isInstanceOf(NoInteractionsWanted.class)
                .hasMessageContainingAll(
                        "No interactions wanted here:",
                        "But found this interaction on mock 'iMethods':");
    }

    @Test
    public void
            can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_no_more_interaction_wanted_in_order() {
        Invocation invocation_with_bogus_default_answer =
                new InvocationBuilder()
                        .mock(mock(IMethods.class, new Returns(false)))
                        .toInvocation();

        assertThatThrownBy(
                        () -> {
                            throw Reporter.noMoreInteractionsWantedInOrder(
                                    invocation_with_bogus_default_answer);
                        })
                .isInstanceOf(VerificationInOrderFailure.class)
                .hasMessageContainingAll(
                        "No interactions wanted here:",
                        "But found this interaction on mock 'iMethods':");
    }

    @Test
    public void
            can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_invalid_argument_position() {
        Invocation invocation_with_bogus_default_answer =
                new InvocationBuilder()
                        .mock(mock(IMethods.class, new Returns(false)))
                        .toInvocation();

        assertThatThrownBy(
                        () -> {
                            throw Reporter.invalidArgumentPositionRangeAtInvocationTime(
                                    invocation_with_bogus_default_answer, true, 0);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Invalid argument index for the current invocation of method :",
                        " -> iMethods.simpleMethod()",
                        "Last parameter wanted but the method has no arguments.");
    }

    @Test
    public void
            can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_wrong_argument_to_return() {
        Invocation invocation_with_bogus_default_answer =
                new InvocationBuilder()
                        .mock(mock(IMethods.class, new Returns(false)))
                        .toInvocation();

        assertThatThrownBy(
                        () -> {
                            throw Reporter.wrongTypeOfArgumentToReturn(
                                    invocation_with_bogus_default_answer, "", String.class, 0);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "The argument of type 'String' cannot be returned because the following",
                        "method should return the type ''",
                        " -> iMethods.simpleMethod()",
                        "The reason for this error can be :",
                        "1. The wanted argument position is incorrect.",
                        "2. The answer is used on the wrong interaction.",
                        "Position of the wanted argument is 0 and the method has no arguments.");
    }

    @Test
    public void
            can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_delegate_method_dont_exists() {
        Invocation dumb_invocation = new InvocationBuilder().toInvocation();
        IMethods mock_with_bogus_default_answer = mock(IMethods.class, new Returns(false));

        assertThatThrownBy(
                        () -> {
                            throw Reporter.delegatedMethodDoesNotExistOnDelegate(
                                    dumb_invocation.getMethod(),
                                    mock_with_bogus_default_answer,
                                    String.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Methods called on mock must exist in delegated instance.",
                        "When calling: public abstract java.lang.String org.mockitousage.IMethods.simpleMethod() on mock: iMethods",
                        "no such method was found.",
                        "Check that the instance passed to delegatesTo() is of the correct type or contains compatible methods",
                        "(delegate instance had type: Class)");
    }

    @Test
    public void
            can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_delegate_method_has_wrong_return_type() {
        Invocation dumb_invocation = new InvocationBuilder().toInvocation();
        IMethods mock_with_bogus_default_answer = mock(IMethods.class, new Returns(false));

        assertThatThrownBy(
                        () -> {
                            throw Reporter.delegatedMethodHasWrongReturnType(
                                    dumb_invocation.getMethod(),
                                    dumb_invocation.getMethod(),
                                    mock_with_bogus_default_answer,
                                    String.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Methods called on delegated instance must have compatible return types with the mock.",
                        "When calling: public abstract java.lang.String org.mockitousage.IMethods.simpleMethod() on mock: iMethods",
                        "return type should be: String, but was: String",
                        "Check that the instance passed to delegatesTo() is of the correct type or contains compatible methods",
                        "(delegate instance had type: Class)");
    }

    @Test
    public void
            can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_injection_failure() {
        IMethods mock_with_bogus_default_answer = mock(IMethods.class, new Returns(false));

        assertThatThrownBy(
                        () -> {
                            throw Reporter.cannotInjectDependency(
                                    someField(), mock_with_bogus_default_answer, new Exception());
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Mockito couldn't inject mock dependency 'iMethods' on field",
                        "'static final org.mockito.internal.MockitoCore org.mockito.Mockito.MOCKITO_CORE'",
                        "whose type 'org.mockito.Mockito' was annotated by @InjectMocks in your test.",
                        "Also I failed because: null");
    }

    private Field someField() {
        return Mockito.class.getDeclaredFields()[0];
    }
}
