/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.withSettings;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;

public final class SpyStaticTest {

    @Test
    public void testStaticSpyCallsRealMethodByDefault() {
        assertEquals("foo", Dummy.foo());
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            assertEquals("foo", Dummy.foo());
        }
        assertEquals("foo", Dummy.foo());
    }

    @Test
    public void testStaticSpyWithStubbing() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            assertEquals("foobar", Dummy.fooWithArg("bar"));
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            assertEquals("foobar2", Dummy.fooWithArg("bar2"));
        }
    }

    @Test
    public void testStaticSpyWithVerification() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            assertEquals("foo", Dummy.foo());
            spy.verify(Dummy::foo);
        }
    }

    @Test
    public void testStaticSpyVoidMethod() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            Dummy.var1 = null;
            Dummy.fooVoid("bar");
            assertEquals("bar", Dummy.var1);
            spy.verify(() -> Dummy.fooVoid("bar"));
            Dummy.fooVoid("hello");
            assertEquals("hello", Dummy.var1);
            spy.verify(() -> Dummy.fooVoid("hello"));
        }
    }

    @Test
    public void testStaticSpyResetRestoresRealMethods() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            spy.reset();
            assertEquals("foo", Dummy.foo());
        }
    }

    @Test
    public void testStaticSpyScopeRestoresOriginalBehavior() {
        assertEquals("foo", Dummy.foo());
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
        }
        assertEquals("foo", Dummy.foo());
    }

    @Test
    public void testStaticSpyDoesNotAffectDifferentThread() throws InterruptedException {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());

            AtomicReference<String> result = new AtomicReference<>();
            Thread thread = new Thread(() -> result.set(Dummy.foo()));
            thread.start();
            thread.join();

            assertEquals("foo", result.get());
        }
    }

    @Test
    public void testStaticSpyNamedOverload() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class, "mySpy")) {
            assertEquals("foo", Dummy.foo());
            spy.when(Dummy::foo).thenReturn("named");
            assertEquals("named", Dummy.foo());
        }
    }

    @Test
    public void testStaticSpyWithMockSettings() {
        try (MockedStatic<Dummy> spy =
                Mockito.spyStatic(Dummy.class, withSettings().name("settingsSpy"))) {
            assertEquals("foo", Dummy.foo());
            spy.when(Dummy::foo).thenReturn("settings");
            assertEquals("settings", Dummy.foo());
        }
    }

    @Test
    public void testStaticSpyReifiedOverload() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic()) {
            assertEquals("foo", Dummy.foo());
            spy.when(Dummy::foo).thenReturn("reified");
            assertEquals("reified", Dummy.foo());
        }
    }

    @Test
    public void testStaticSpyReifiedNamedOverload() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic("reifiedSpy")) {
            assertEquals("foo", Dummy.foo());
            spy.when(Dummy::foo).thenReturn("reifiedNamed");
            assertEquals("reifiedNamed", Dummy.foo());
        }
    }

    @Test
    public void testStaticSpyReifiedWithMockSettings() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(withSettings().name("reifiedSettings"))) {
            assertEquals("foo", Dummy.foo());
            spy.when(Dummy::foo).thenReturn("reifiedSettings");
            assertEquals("reifiedSettings", Dummy.foo());
        }
    }

    @Test
    public void testStaticSpyVerifyWithTimes() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            Dummy.foo();
            Dummy.foo();
            Dummy.foo();
            spy.verify(Dummy::foo, times(3));
        }
    }

    @Test
    public void testStaticSpyClearInvocations() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.when(Dummy::foo).thenReturn("bar");
            assertEquals("bar", Dummy.foo());
            spy.clearInvocations();
            spy.verifyNoInteractions();
            // Stubbing should still be active after clearing invocations
            assertEquals("bar", Dummy.foo());
        }
    }

    @Test
    public void testStaticSpyVerifyNoMoreInteractions() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            assertEquals("foo", Dummy.foo());
            spy.verify(Dummy::foo);
            spy.verifyNoMoreInteractions();
        }
    }

    @Test
    public void testStaticSpyVerifyNoMoreInteractionsFailed() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            assertEquals("foo", Dummy.foo());
            assertThatThrownBy(
                            () -> {
                                spy.verifyNoMoreInteractions();
                            })
                    .isInstanceOf(NoInteractionsWanted.class);
        }
    }

    @Test
    public void testStaticSpyVerifyNoInteractions() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            spy.verifyNoInteractions();
        }
    }

    @Test
    public void testStaticSpyVerifyNoInteractionsFailed() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            Dummy.foo();
            assertThatThrownBy(
                            () -> {
                                spy.verifyNoInteractions();
                            })
                    .isInstanceOf(NoInteractionsWanted.class);
        }
    }

    @Test
    public void testStaticSpyVerifyFailed() {
        try (MockedStatic<Dummy> spy = Mockito.spyStatic(Dummy.class)) {
            assertThatThrownBy(
                            () -> {
                                spy.verify(Dummy::foo);
                            })
                    .isInstanceOf(WantedButNotInvoked.class);
        }
    }

    @Test
    public void testStaticSpyReifiedWithMockSettingsRejectsExplicitValues() {
        assertThatThrownBy(
                        () -> {
                            Mockito.spyStatic(withSettings().name("bad"), new Dummy());
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Please don't pass any values here");
    }

    @Test
    public void testStaticSpyReifiedWithMockSettingsRejectsNull() {
        assertThatThrownBy(
                        () -> {
                            Mockito.spyStatic(withSettings().name("bad"), (Dummy[]) null);
                        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Please don't pass any values here");
    }

    @Test
    public void testStaticSpyMustBeExclusiveInScopeWithinThread() {
        assertThatThrownBy(
                        () -> {
                            try (MockedStatic<Dummy> spy1 = Mockito.spyStatic(Dummy.class);
                                    MockedStatic<Dummy> spy2 = Mockito.spyStatic(Dummy.class)) {}
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("static mocking is already registered in the current thread");
    }

    static class Dummy {

        static String var1 = null;

        static String foo() {
            return "foo";
        }

        static String fooWithArg(String arg) {
            return "foo" + arg;
        }

        static void fooVoid(String var2) {
            var1 = var2;
        }
    }
}
