/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.exceptions;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.mockito.Mockito.mock;

public class ReporterTest extends TestBase {

    @Test(expected = TooLittleActualInvocations.class)
    public void should_let_passing_null_last_actual_stack_trace() throws Exception {
        throw Reporter.tooLittleActualInvocations(new org.mockito.internal.reporting.Discrepancy(1, 2), new InvocationBuilder().toInvocation(), null);
    }

    @Test(expected = MockitoException.class)
    public void should_throw_correct_exception_for_null_invocation_listener() throws Exception {
        throw Reporter.methodDoesNotAcceptParameter("invocationListeners", "null vararg array");
    }

    @Test(expected = NoInteractionsWanted.class)
    public void can_use_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_no_more_interaction_wanted() throws Exception {
        Invocation invocation_with_bogus_default_answer = new InvocationBuilder().mock(mock(IMethods.class, new Returns(false))).toInvocation();
        throw Reporter.noMoreInteractionsWanted(invocation_with_bogus_default_answer, Collections.<VerificationAwareInvocation>emptyList());
    }

    @Test(expected = VerificationInOrderFailure.class)
    public void can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_no_more_interaction_wanted_in_order() throws Exception {
        Invocation invocation_with_bogus_default_answer = new InvocationBuilder().mock(mock(IMethods.class, new Returns(false))).toInvocation();
        throw Reporter.noMoreInteractionsWantedInOrder(invocation_with_bogus_default_answer);
    }

    @Test(expected = MockitoException.class)
    public void can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_invalid_argument_position() throws Exception {
        Invocation invocation_with_bogus_default_answer = new InvocationBuilder().mock(mock(IMethods.class, new Returns(false))).toInvocation();
        throw Reporter.invalidArgumentPositionRangeAtInvocationTime(invocation_with_bogus_default_answer, true, 0);
    }

    @Test(expected = MockitoException.class)
    public void can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_wrong_argument_to_return() throws Exception {
        Invocation invocation_with_bogus_default_answer = new InvocationBuilder().mock(mock(IMethods.class, new Returns(false))).toInvocation();
        throw Reporter.wrongTypeOfArgumentToReturn(invocation_with_bogus_default_answer, "", String.class, 0);
    }

    @Test(expected = MockitoException.class)
    public void can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_delegate_method_dont_exists() throws Exception {
        Invocation dumb_invocation = new InvocationBuilder().toInvocation();
        IMethods mock_with_bogus_default_answer = mock(IMethods.class, new Returns(false));
        throw Reporter.delegatedMethodDoesNotExistOnDelegate(dumb_invocation.getMethod(), mock_with_bogus_default_answer, String.class);
    }

    @Test(expected = MockitoException.class)
    public void can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_delegate_method_has_wrong_return_type() throws Exception {
        Invocation dumb_invocation = new InvocationBuilder().toInvocation();
        IMethods mock_with_bogus_default_answer = mock(IMethods.class, new Returns(false));
        throw Reporter.delegatedMethodHasWrongReturnType(dumb_invocation.getMethod(), dumb_invocation.getMethod(), mock_with_bogus_default_answer, String.class);
    }

    @Test(expected = MockitoException.class)
    public void can_use_print_mock_name_even_when_mock_bogus_default_answer_and_when_reporting_injection_failure() throws Exception {
        IMethods mock_with_bogus_default_answer = mock(IMethods.class, new Returns(false));
        throw Reporter.cannotInjectDependency(someField(), mock_with_bogus_default_answer, new Exception());
    }

    private Field someField() {
        return Mockito.class.getDeclaredFields()[0];
    }

}
