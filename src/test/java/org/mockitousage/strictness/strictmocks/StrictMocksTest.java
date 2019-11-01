/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.strictness.strictmocks;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockitoutil.TestBase.filterLineNo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.exceptions.misusing.UnstubbedInvocation;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitousage.strictness.ProductionCode;

public class StrictMocksTest {

    MockitoSession mockito;
    @Mock IMethods mock;

    @Before
    public void before() {
        mockito = Mockito.mockitoSession().initMocks(this).strictness(Strictness.STRICT_MOCKS).startMocking();
    }

    @Test
    public void non_void_method_needs_to_be_stubbed() {
        //given
        doReturn("foo").when(mock).simpleMethod();
        lenient().doReturn("bar").when(mock).oneArg(10);

        //expect
        mock.voidMethod();  // calling void method is OK
        mock.simpleMethod();  // calling stubbed method is OK

        try {
            mock.otherMethod();
            fail();
        } catch (UnstubbedInvocation e) {
            assertEquals(filterLineNo("\n" +
                    "Unstubbed invocation or incorrect stubbing: mock.otherMethod();\n" +
                    "-> at org.mockitousage.strictness.strictmocks.StrictMocksTest.non_void_method_needs_to_be_stubbed(StrictMocksTest.java:0)\n" +
                    "'STRICT_MOCKS' require:\n" +
                    "  - doReturn()/doThrow() API for stubbing - please ensure you are using the right API\n" +
                    "  - all non-void invocations to be stubbed (e.g. declared in the test) - please stub this invocation in your test or reduce the strictness (see javadoc for Strictness).\n" +
                    "\n" +
                    "There are are exactly 2 stubbings declared on this mock:\n" +
                    "1. mock.simpleMethod(); (used: true)\n" +
                    "-> at org.mockitousage.strictness.strictmocks.StrictMocksTest.non_void_method_needs_to_be_stubbed(StrictMocksTest.java:0)\n" +
                    "\n" +
                    "2. mock.oneArg(10); (used: false)\n" +
                    "-> at org.mockitousage.strictness.strictmocks.StrictMocksTest.non_void_method_needs_to_be_stubbed(StrictMocksTest.java:0)"),
                filterLineNo(e.getMessage()));
        }
    }

    @Test
    public void reports_argument_mismatch() {
        //given
        doReturn("foo").when(mock).simpleMethod(10);

        //expect
        assertThatExceptionOfType(PotentialStubbingProblem.class).isThrownBy(() ->
            ProductionCode.simpleMethod(mock, 20));
    }

    @Test
    public void stubs_are_implicitly_marked_verified() {
        //given
        doReturn("foo").when(mock).simpleMethod();

        //when
        mock.simpleMethod();

        //then
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void void_methods_are_not_implicitly_marked_verified() {
        //given
        mock.voidMethod();

        //expect
        assertThatExceptionOfType (NoInteractionsWanted.class).isThrownBy(() -> verifyNoMoreInteractions(mock));
    }

    @Test
    public void unused_stubbings_are_reported() {
        //given
        doReturn("foo").when(mock).simpleMethod();

        //expect
        assertThatExceptionOfType(UnnecessaryStubbingException.class).isThrownBy(() -> mockito.finishMocking());
    }

    @Test
    public void lenient_unused_stubbing_is_not_reported() {
        //given
        lenient().doReturn("foo").when(mock).simpleMethod();

        //expect no exception here:
        mockito.finishMocking();
    }

    @Test
    public void unstubbed_invocation_overrides_unused_stubbing() {
        //given
        doReturn("foo").when(mock).simpleMethod(); // <- unused

        //expect
        assertThatExceptionOfType(UnstubbedInvocation.class).isThrownBy(() -> mock.otherMethod());

        //and no exception when:
        mockito.finishMocking();
    }

    @After
    public void after() {
        mockito.finishMocking();
    }
}
