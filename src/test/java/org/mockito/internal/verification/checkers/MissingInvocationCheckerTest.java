/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static java.util.Arrays.asList;

import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class MissingInvocationCheckerTest extends TestBase {

	private InvocationMatcher wanted;
	private List<Invocation> invocations;

	@Mock
	private IMethods mock;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldPassBecauseActualInvocationFound() {
		wanted = buildSimpleMethod().toInvocationMatcher();
		invocations = asList(buildSimpleMethod().toInvocation());

		MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
	}

	@Test
	public void shouldReportWantedButNotInvoked() {
		wanted = buildSimpleMethod().toInvocationMatcher();
		invocations = asList(buildDifferentMethod().toInvocation());

		exception.expect(WantedButNotInvoked.class);
		exception.expectMessage("Wanted but not invoked:");
		exception.expectMessage("mock.simpleMethod()");
		exception.expectMessage("However, there was exactly 1 interaction with this mock:");
		exception.expectMessage("mock.differentMethod();");

		MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
	}

	@Test
	public void shouldReportWantedInvocationDiffersFromActual() {
		wanted = buildIntArgMethod().arg(2222).toInvocationMatcher();
		invocations = asList(buildIntArgMethod().arg(1111).toInvocation());

		exception.expect(ArgumentsAreDifferent.class);

		exception.expectMessage("Argument(s) are different! Wanted:");
		exception.expectMessage("mock.intArgumentMethod(2222);");
		exception.expectMessage("Actual invocation has different arguments:");
		exception.expectMessage("mock.intArgumentMethod(1111);");

		MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
	}

	private InvocationBuilder buildIntArgMethod() {
		return new InvocationBuilder().mock(mock).method("intArgumentMethod").argTypes(int.class);
	}

	private InvocationBuilder buildSimpleMethod() {
		return new InvocationBuilder().mock(mock).simpleMethod();
	}

	private InvocationBuilder buildDifferentMethod() {
		return new InvocationBuilder().mock(mock).differentMethod();
	}
}
