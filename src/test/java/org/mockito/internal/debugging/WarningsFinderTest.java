/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class WarningsFinderTest extends TestBase {

    @Mock private IMethods mock;
    @Mock private FindingsListener listener;

    @Test
    public void shouldPrintUnusedStub() {
        // given
        Invocation unusedStub = new InvocationBuilder().simpleMethod().toInvocation();

        // when
        WarningsFinder finder = new WarningsFinder(asList(unusedStub), Arrays.<InvocationMatcher>asList());
        finder.find(listener);

        // then
        verify(listener, only()).foundUnusedStub(unusedStub);
    }

    @Test
    public void shouldPrintUnstubbedInvocation() {
        // given
        InvocationMatcher unstubbedInvocation = new InvocationBuilder().differentMethod().toInvocationMatcher();

        // when
        WarningsFinder finder = new WarningsFinder(Arrays.<Invocation>asList(), Arrays.<InvocationMatcher>asList(unstubbedInvocation));
        finder.find(listener);

        // then
        verify(listener, only()).foundUnstubbed(unstubbedInvocation);
    }

    @Test
    public void shouldPrintStubWasUsedWithDifferentArgs() {
        // given
        Invocation stub = new InvocationBuilder().arg("foo").mock(mock).toInvocation();
        InvocationMatcher wrongArg = new InvocationBuilder().arg("bar").mock(mock).toInvocationMatcher();

        // when
        WarningsFinder finder = new WarningsFinder(Arrays.<Invocation> asList(stub), Arrays.<InvocationMatcher> asList(wrongArg));
        finder.find(listener);

        // then
        verify(listener, only()).foundStubCalledWithDifferentArgs(stub, wrongArg);
    }
}