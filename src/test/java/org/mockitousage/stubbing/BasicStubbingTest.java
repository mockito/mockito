/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.CannotVerifyStubOnlyMock;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class BasicStubbingTest extends TestBase {

    private IMethods mock;

    @Before
    public void setup() {
        mock = mock(IMethods.class);
    }

    @Test
    public void should_evaluate_latest_stubbing_first() throws Exception {
        when(mock.objectReturningMethod(isA(Integer.class))).thenReturn(100);
        when(mock.objectReturningMethod(200)).thenReturn(200);

        assertEquals(200, mock.objectReturningMethod(200));
        assertEquals(100, mock.objectReturningMethod(666));
        assertEquals(
                "default behavior should return null", null, mock.objectReturningMethod("blah"));
    }

    @Test
    public void should_stubbing_be_treated_as_interaction() throws Exception {
        when(mock.booleanReturningMethod()).thenReturn(true);

        mock.booleanReturningMethod();

        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
        }
    }

    @Test
    public void should_allow_stubbing_to_string() throws Exception {
        IMethods mockTwo = mock(IMethods.class);
        when(mockTwo.toString()).thenReturn("test");

        assertThat(mock.toString()).contains("Mock for IMethods");
        assertThat(mockTwo.toString()).isEqualTo("test");
    }

    @Test
    public void should_stubbing_not_be_treated_as_interaction() {
        when(mock.simpleMethod("one")).thenThrow(new RuntimeException());
        doThrow(new RuntimeException()).when(mock).simpleMethod("two");

        verifyZeroInteractions(mock);
    }

    @Test
    public void should_stubbing_not_be_treated_as_interaction_verify_no_interactions() {
        when(mock.simpleMethod("one")).thenThrow(new RuntimeException());
        doThrow(new RuntimeException()).when(mock).simpleMethod("two");

        verifyNoInteractions(mock);
    }

    @Test
    public void unfinished_stubbing_cleans_up_the_state() {
        reset(mock);
        try {
            when("").thenReturn("");
            fail();
        } catch (MissingMethodInvocationException e) {
        }

        // anything that can cause state validation
        verifyZeroInteractions(mock);
    }

    @Test
    public void unfinished_stubbing_cleans_up_the_state_verify_no_interactions() {
        reset(mock);
        try {
            when("").thenReturn("");
            fail();
        } catch (MissingMethodInvocationException e) {
        }

        // anything that can cause state validation
        verifyNoInteractions(mock);
    }

    @Test
    public void should_to_string_mock_name() {
        IMethods mock = mock(IMethods.class, "mockie");
        IMethods mockTwo = mock(IMethods.class);

        assertThat(mockTwo.toString()).contains("Mock for IMethods");
        assertEquals("mockie", "" + mock);
    }

    class Foo {
        public final String toString() {
            return "foo";
        }
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void should_allow_mocking_when_to_string_is_final() throws Exception {
        mock(Foo.class);
    }

    @Test
    public void test_stub_only_not_verifiable() throws Exception {
        IMethods localMock = mock(IMethods.class, withSettings().stubOnly());

        when(localMock.objectReturningMethod(isA(Integer.class))).thenReturn(100);
        when(localMock.objectReturningMethod(200)).thenReturn(200);

        assertEquals(200, localMock.objectReturningMethod(200));
        assertEquals(100, localMock.objectReturningMethod(666));
        assertEquals(
                "default behavior should return null",
                null,
                localMock.objectReturningMethod("blah"));

        try {
            verify(localMock, atLeastOnce()).objectReturningMethod(eq(200));
            fail();
        } catch (CannotVerifyStubOnlyMock e) {
        }
    }

    @SuppressWarnings("MockitoUsage")
    @Test
    public void test_stub_only_not_verifiable_fail_fast() {
        IMethods localMock = mock(IMethods.class, withSettings().stubOnly());

        try {
            verify(localMock); // throws exception before method invocation
            fail();
        } catch (CannotVerifyStubOnlyMock e) {
            assertEquals(
                    "\n"
                            + "Argument \"iMethods\" passed to verify is a stubOnly() mock which cannot be verified.\n"
                            + "If you intend to verify invocations on this mock, don't use stubOnly() in its MockSettings.",
                    e.getMessage());
        }
    }

    @Test
    public void test_stub_only_not_verifiable_verify_no_more_interactions() {
        IMethods localMock = mock(IMethods.class, withSettings().stubOnly());

        try {
            verifyNoMoreInteractions(localMock);
            fail();
        } catch (CannotVerifyStubOnlyMock e) {
        }
    }

    @Test
    public void test_stub_only_not_verifiable_in_order() {
        IMethods localMock = mock(IMethods.class, withSettings().stubOnly());

        try {
            inOrder(localMock);
            fail();
        } catch (CannotVerifyStubOnlyMock e) {
        }
    }
}
