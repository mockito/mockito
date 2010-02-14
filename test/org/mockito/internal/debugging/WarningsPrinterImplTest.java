/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import static java.util.Arrays.*;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.util.MockitoLoggerStub;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class WarningsPrinterImplTest extends TestBase {

    @Mock
    private IMethods mock;
    private MockitoLoggerStub logger = new MockitoLoggerStub();

    //TODO those tests should only deal with mocks

    @Test
    public void shouldPrintUnusedStub() {
        // given
        Invocation unusedStub = new InvocationBuilder().simpleMethod().toInvocation();
        WarningsPrinterImpl p = new WarningsPrinterImpl(asList(unusedStub), Arrays.<InvocationMatcher> asList());

        // when
        p.print(logger);

        // then
        assertContains("never used", logger.getLoggedInfo());
    }

    @Test
    public void shouldPrintUnstubbedInvocation() {
        // given
        InvocationMatcher unstubbedInvocation = new InvocationBuilder().differentMethod().toInvocationMatcher();
        WarningsPrinterImpl p = new WarningsPrinterImpl(Arrays.<Invocation> asList(), Arrays.<InvocationMatcher> asList(unstubbedInvocation), true);

        // when
        p.print(logger);

        // then
        assertContains("was not stubbed", logger.getLoggedInfo());
        assertContains("differentMethod()", logger.getLoggedInfo());
    }

    @Test
    public void shouldPrintStubWasUsedWithDifferentArgs() {
        // given
        Invocation stub = new InvocationBuilder().arg("foo").mock(mock).toInvocation();
        InvocationMatcher wrongArg = new InvocationBuilder().arg("bar").mock(mock).toInvocationMatcher();

        WarningsPrinterImpl p = new WarningsPrinterImpl(Arrays.<Invocation> asList(stub), Arrays.<InvocationMatcher> asList(wrongArg));

        // when
        p.print(logger);

        // then
        assertContains("different arg", logger.getLoggedInfo());
    }

    @Test
    public void shouldNotPrintRedundantInformation() {
        // given
        Invocation stub = new InvocationBuilder().arg("foo").mock(mock).toInvocation();
        InvocationMatcher wrongArg = new InvocationBuilder().arg("bar").mock(mock).toInvocationMatcher();

        WarningsPrinterImpl p = new WarningsPrinterImpl(Arrays.<Invocation> asList(stub), Arrays.<InvocationMatcher> asList(wrongArg));

        // when
        p.print(logger);

        // then
        assertNotContains("stub was not used", logger.getLoggedInfo());
        assertNotContains("was not stubbed", logger.getLoggedInfo());
    }
}