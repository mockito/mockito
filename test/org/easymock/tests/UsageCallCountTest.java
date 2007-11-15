/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests;

import static org.junit.Assert.*;

import org.easymock.MockControl;
import org.junit.Before;
import org.junit.Test;

public class UsageCallCountTest {

    private MockControl<VoidMethodInterface> control;

    private VoidMethodInterface mock;

    private interface VoidMethodInterface {
        void method();
    }

    @Before
    public void setup() {
        control = MockControl.createControl(VoidMethodInterface.class);
        mock = control.getMock();
    }

    @Test
    public void mockWithNoExpectedCallsPassesWithNoCalls() {
        control.replay();
        control.verify();
    }

    @Test
    public void mockWithNoExpectedCallsFailsAtFirstCall() {
        control.replay();
        assertMethodCallFails();
    }

    @Test
    public void mockWithOneExpectedCallFailsAtVerify() {
        callMethodOnce();
        control.replay();
        assertVerifyFails();
    }

    @Test
    public void mockWithOneExpectedCallPassesWithOneCall() {
        callMethodOnce();
        control.replay();
        callMethodOnce();
        control.verify();
    }

    @Test
    public void mockWithOneExpectedCallFailsAtSecondCall() {
        callMethodOnce();
        control.replay();
        callMethodOnce();
        assertMethodCallFails();
    }

    @Test
    public void tooFewCalls() {
        callMethodThreeTimes();
        control.replay();
        callMethodTwice();
        assertVerifyFails();
    }

    @Test
    public void correctNumberOfCalls() {
        callMethodThreeTimes();
        control.replay();
        callMethodThreeTimes();
        control.verify();
    }

    @Test
    public void tooManyCalls() {
        callMethodThreeTimes();
        control.replay();
        callMethodThreeTimes();
        assertMethodCallFails();
    }

    private void callMethodOnce() {
        mock.method();
    }

    private void callMethodTwice() {
        mock.method();
        mock.method();
    }

    private void callMethodThreeTimes() {
        mock.method();
        mock.method();
        mock.method();
    }

    private void assertVerifyFails() {
        try {
            control.verify();
            fail("Expected AssertionError");
        } catch (AssertionError expected) {
        }
    }

    private void assertMethodCallFails() {
        try {
            mock.method();
            fail("Expected AssertionError");
        } catch (AssertionError expected) {
        }
    }

    @Test
    public void noUpperLimitWithoutCallCountSet() {
        mock.method();
        control.setVoidCallable(MockControl.ONE_OR_MORE);
        control.replay();
        assertVerifyFails();
        mock.method();
        control.verify();
        mock.method();
        control.verify();
        mock.method();
        control.verify();
    }
}
