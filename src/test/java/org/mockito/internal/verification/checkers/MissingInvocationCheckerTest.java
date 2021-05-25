/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.internal.invocation.*;
import org.mockito.internal.invocation.mockref.MockReference;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class MissingInvocationCheckerTest extends TestBase {

    private InvocationMatcher wanted;
    private List<Invocation> invocations;

    @Mock private IMethods mock;

    @Rule public ExpectedException exception = ExpectedException.none();

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
        wanted = buildIntArgMethod(new InvocationBuilder()).arg(2222).toInvocationMatcher();
        invocations = asList(buildIntArgMethod(new InvocationBuilder()).arg(1111).toInvocation());

        exception.expect(ArgumentsAreDifferent.class);

        exception.expectMessage("Argument(s) are different! Wanted:");
        exception.expectMessage("mock.intArgumentMethod(2222);");
        exception.expectMessage("Actual invocations have different arguments:");
        exception.expectMessage("mock.intArgumentMethod(1111);");

        MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
    }

    @Test
    public void shouldReportUsingInvocationDescription() {
        wanted = buildIntArgMethod(new CustomInvocationBuilder()).arg(2222).toInvocationMatcher();
        invocations =
                singletonList(
                        buildIntArgMethod(new CustomInvocationBuilder()).arg(1111).toInvocation());

        exception.expect(ArgumentsAreDifferent.class);

        exception.expectMessage("Argument(s) are different! Wanted:");
        exception.expectMessage("mock.intArgumentMethod(MyCoolPrint(2222));");
        exception.expectMessage("Actual invocations have different arguments:");
        exception.expectMessage("mock.intArgumentMethod(MyCoolPrint(1111));");

        MissingInvocationChecker.checkMissingInvocation(invocations, wanted);
    }

    private InvocationBuilder buildIntArgMethod(InvocationBuilder invocationBuilder) {
        return invocationBuilder.mock(mock).method("intArgumentMethod").argTypes(int.class);
    }

    private InvocationBuilder buildSimpleMethod() {
        return new InvocationBuilder().mock(mock).simpleMethod();
    }

    private InvocationBuilder buildDifferentMethod() {
        return new InvocationBuilder().mock(mock).differentMethod();
    }

    static class CustomInvocationBuilder extends InvocationBuilder {
        @Override
        protected Invocation createInvocation(
                MockReference<Object> mockRef,
                MockitoMethod mockitoMethod,
                final Object[] arguments,
                RealMethod realMethod,
                Location location,
                int sequenceNumber) {
            return new InterceptedInvocation(
                    mockRef, mockitoMethod, arguments, realMethod, location, sequenceNumber) {
                @Override
                public List<ArgumentMatcher> getArgumentsAsMatchers() {
                    List<ArgumentMatcher> matchers = new ArrayList<ArgumentMatcher>();
                    for (final Object argument : getRawArguments()) {
                        matchers.add(
                                new ArgumentMatcher() {
                                    @Override
                                    public boolean matches(Object a) {
                                        return a == argument;
                                    }

                                    @Override
                                    public String toString() {
                                        return "MyCoolPrint(" + argument + ")";
                                    }
                                });
                    }
                    return matchers;
                }
            };
        }
    }
}
