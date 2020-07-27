/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.times;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;

public final class StaticMockTest {

    @Test
    public void testStaticMockSimple() {
        assertEquals("foo", Dummy.foo());
        try (MockedStatic<Dummy> ignored = Mockito.mockStatic(Dummy.class)) {
            assertNull(Dummy.foo());
        }
        assertEquals("foo", Dummy.foo());
    }

    @Test
    public void testStaticMockWithVerification() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
        }
    }

    @Test(expected = WantedButNotInvoked.class)
    public void testStaticMockWithVerificationFailed() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.verify(Dummy::foo);
        }
    }

    @Test
    public void testStaticMockWithMoInteractions() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            dummy.verifyNoInteractions();
        }
    }

    @Test(expected = NoInteractionsWanted.class)
    public void testStaticMockWithMoInteractionsFailed() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verifyNoInteractions();
        }
    }

    @Test
    public void testStaticMockWithMoMoreInteractions() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
            dummy.verifyNoMoreInteractions();
        }
    }

    @Test(expected = NoInteractionsWanted.class)
    public void testStaticMockWithMoMoreInteractionsFailed() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testStaticMockWithDefaultAnswer() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class, invocation -> "bar")) {
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
        }
    }

    @Test
    public void testStaticMockWithRealMethodCall() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenCallRealMethod();
            assertEquals("foo", Dummy.foo());
            dummy.verify(Dummy::foo);
        }
    }

    @Test
    public void testStaticMockReset() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            dummy.reset();
            assertNull(Dummy.foo());
        }
    }

    @Test
    public void testStaticMockClear() {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.clearInvocations();
            dummy.verifyNoInteractions();
        }
    }

    @Test
    public void testStaticMockDoesNotAffectDifferentThread() throws InterruptedException {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
            AtomicReference<String> reference = new AtomicReference<>();
            Thread thread = new Thread(() -> reference.set(Dummy.foo()));
            thread.start();
            thread.join();
            assertEquals("foo", reference.get());
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(times(2), Dummy::foo);
        }
    }

    @Test
    public void testStaticMockCanCoexistWithMockInDifferentThread() throws InterruptedException {
        try (MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class)) {
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(Dummy::foo);
            AtomicReference<String> reference = new AtomicReference<>();
            Thread thread = new Thread(() -> {
                try (MockedStatic<Dummy> dummy2 = Mockito.mockStatic(Dummy.class)) {
                    dummy2.when(Dummy::foo).thenReturn("qux");
                    reference.set(Dummy.foo());
                }
            });
            thread.start();
            thread.join();
            assertEquals("qux", reference.get());
            dummy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            dummy.verify(times(2), Dummy::foo);
        }
    }

    @Test(expected = MockitoException.class)
    public void testStaticMockMustBeExclusiveInScopeWithinThread() {
        try (
            MockedStatic<Dummy> dummy = Mockito.mockStatic(Dummy.class);
            MockedStatic<Dummy> duplicate = Mockito.mockStatic(Dummy.class)
        ) {
            fail("Not supposed to allow duplicates");
        }
    }

    static class Dummy {

        static String foo() {
            return "foo";
        }
    }
}
